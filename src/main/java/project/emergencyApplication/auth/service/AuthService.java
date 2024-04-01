package project.emergencyApplication.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.emergencyApplication.auth.dto.*;
import project.emergencyApplication.auth.entity.RefreshToken;
import project.emergencyApplication.auth.exception.NotFoundMemberException;
import project.emergencyApplication.auth.jwt.JwtTokenProvider;
import project.emergencyApplication.auth.jwt.apple.AppleOAuthUserProvider;
import project.emergencyApplication.auth.repository.RefreshTokenRepository;
import project.emergencyApplication.domain.member.entity.Member;
import project.emergencyApplication.domain.member.entity.Platform;
import project.emergencyApplication.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final AppleOAuthUserProvider appleOAuthUserProvider;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;


    public TokenDto appleOAuthLogin(AppleLoginRequest request) {

        // platformId, name, email 가져오기
        OAuthPlatformMemberResponse applePlatformMember = appleOAuthUserProvider.getApplePlatformMember(request);
        return generateOAuthTokenResponse(applePlatformMember);
    }

    private TokenDto generateOAuthTokenResponse(OAuthPlatformMemberResponse applePlatformMember) {

        // Platform 과 email 을 기반으로 회원 조회
        return memberRepository.findByPlatformAndEmail(Platform.APPLE, applePlatformMember.getEmail())
                .map(memberId -> {  /** 기존 회원인 경우 */
                    Member findMember = memberRepository.findById(memberId)
                            .orElseThrow(NotFoundMemberException::new);

                    // email 을 기반으로 Authentication 생성
                    Authentication authentication = createAuthentication(applePlatformMember);

                    // 인증 정보를 기반으로 TokenDto 를 생성한 뒤 RefreshToken 저장
                    TokenDto tokenDto = getTokenDto(authentication, findMember.getEmail());

                    // 토큰 발급
                    return tokenDto;
                })
                .orElseGet(() -> {  /** 신규 회원인 경우 */
                    // 회원 생성 및 저장
                    Member oauthMember = applePlatformMember.createMember(passwordEncoder);
                    Member saveMember = memberRepository.save(oauthMember);

                    // email 을 기반으로 Authentication 생성
                    Authentication authentication = createAuthentication(applePlatformMember);

                    // 인증 정보를 기반으로 TokenDto 를 생성한 뒤 RefreshToken 저장
                    TokenDto tokenDto = getTokenDto(authentication, saveMember.getEmail());

                    // 토큰 발급
                    return tokenDto;
                });
    }

    private TokenDto getTokenDto(Authentication authentication, String email) {
        // 인증 정보를 기반으로 JWT 생성
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication, email);

        // RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);
        return tokenDto;
    }

    private Authentication createAuthentication(OAuthPlatformMemberResponse applePlatformMember) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(applePlatformMember.getEmail(), applePlatformMember.getPlatformId());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }

}