package project.emergencyApplication.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.emergencyApplication.auth.dto.AppleLoginRequest;
import project.emergencyApplication.auth.dto.OAuthPlatformMemberResponse;
import project.emergencyApplication.auth.dto.OAuthTokenResponse;
import project.emergencyApplication.auth.dto.Token;
import project.emergencyApplication.auth.exception.NotFoundMemberException;
import project.emergencyApplication.auth.jwt.JwtTokenProvider;
import project.emergencyApplication.auth.jwt.apple.AppleOAuthUserProvider;
import project.emergencyApplication.domain.member.entity.Member;
import project.emergencyApplication.domain.member.repository.MemberRepository;
import project.emergencyApplication.domain.platform.Platform;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final AppleOAuthUserProvider appleOAuthUserProvider;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public OAuthTokenResponse appleOAuthLogin(AppleLoginRequest request) {
        OAuthPlatformMemberResponse applePlatformMember =
                appleOAuthUserProvider.getApplePlatformMember(request.getId_token());
        return generateOAuthTokenResponse(
            Platform.APPLE,
            applePlatformMember.getEmail()
        );
    }

    private OAuthTokenResponse generateOAuthTokenResponse(Platform platform, String email) {

        Authentication authentication = createAuthentication(email);

        return memberRepository.findByPlatformAndEmail(platform, email)
                .map(memberId -> {  /** 기존 회원 */
                    Member findMember = memberRepository.findById(memberId)
                            .orElseThrow(NotFoundMemberException::new);
                    String accessToken = issueAccessToken(authentication);
                    String refreshToken = issueRefreshToken();

                    refreshTokenService.saveTokenInfo(findMember.getMemberId(), refreshToken, accessToken);

                    return new OAuthTokenResponse(accessToken, refreshToken, findMember.getEmail(), true);
                })
                .orElseGet(() -> {  /** 신규 회원 */
                    Member oauthMember = new Member(email, platform);
                    Member savedMember = memberRepository.save(oauthMember);
                    String accessToken = issueAccessToken(authentication);
                    String refreshToken = issueRefreshToken();

                    refreshTokenService.saveTokenInfo(savedMember.getMemberId(), refreshToken, accessToken);
                    return new OAuthTokenResponse(accessToken, refreshToken, email, false);
                });
    }

    private Authentication createAuthentication(String email) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, "");

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }

    private String issueAccessToken(Authentication authentication) {
        return jwtTokenProvider.createAccessToken(authentication);
    }

    private String issueRefreshToken() {
        return Token.createRefreshToken();
    }

}
