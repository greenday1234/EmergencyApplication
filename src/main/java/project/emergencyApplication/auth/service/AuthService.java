package project.emergencyApplication.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import project.emergencyApplication.auth.dto.*;
import project.emergencyApplication.auth.entity.RefreshToken;
import project.emergencyApplication.auth.exception.NotFoundMemberException;
import project.emergencyApplication.auth.jwt.JwtTokenProvider;
import project.emergencyApplication.auth.jwt.apple.AppleOAuthUserProvider;
import project.emergencyApplication.auth.repository.RefreshTokenRepository;
import project.emergencyApplication.domain.member.entity.Member;
import project.emergencyApplication.domain.member.entity.Platform;
import project.emergencyApplication.domain.member.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final AppleOAuthUserProvider appleOAuthUserProvider;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public TokenDto appleOAuthLogin(AppleLoginRequest request) {

        // platformId, name, email, deviceToken 가져오기
        OAuthPlatformMemberResponse applePlatformMember = appleOAuthUserProvider.getApplePlatformMember(request);
        return generateOAuthTokenResponse(applePlatformMember);
    }

    @Transactional
    private TokenDto generateOAuthTokenResponse(OAuthPlatformMemberResponse applePlatformMember) {

        // Platform 과 email 을 기반으로 회원 조회
        return memberRepository.findByPlatformAndEmail(Platform.APPLE, applePlatformMember.getEmail())
                .map(memberId -> {  /** 기존 회원인 경우 */
                    memberRepository.findById(memberId)
                            .orElseThrow(NotFoundMemberException::new);

                    // email 을 기반으로 Authentication 생성, authentication.getName() 은 MemberId
                    // CustomUserDetailsService 에서 MemberId 가 들어가도록 설정함
                    Authentication authentication = createAuthentication(applePlatformMember);

                    // 인증 정보를 기반으로 TokenDto 를 생성한 뒤 RefreshToken 저장
                    TokenDto tokenDto = getTokenDto(authentication);

                    // 토큰 발급
                    return tokenDto;
                })
                .orElseGet(() -> {  /** 신규 회원인 경우 */
                    // 회원 생성 및 저장
                    Member oauthMember = applePlatformMember.createMember(passwordEncoder);
                    memberRepository.save(oauthMember);

                    // email 을 기반으로 Authentication 생성, authentication.getName() 은 MemberId
                    // CustomUserDetailsService 에서 MemberId 가 들어가도록 설정함
                    Authentication authentication = createAuthentication(applePlatformMember);

                    // 인증 정보를 기반으로 TokenDto 를 생성한 뒤 RefreshToken 저장
                    TokenDto tokenDto = getTokenDto(authentication);

                    // 토큰 발급
                    return tokenDto;
                });
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {

        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져오기
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        refreshToken.updateValue(tokenDto.getRefreshToken());

        // 토큰 발급
        return tokenDto;
    }

    @Transactional
    private TokenDto getTokenDto(Authentication authentication) {
        // 인증 정보를 기반으로 JWT 생성
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication);

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