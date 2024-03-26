package project.emergencyApplication.auth.service;

import lombok.RequiredArgsConstructor;
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

    public OAuthTokenResponse appleOAuthLogin(AppleLoginRequest request) {
        OAuthPlatformMemberResponse applePlatformMember =
                appleOAuthUserProvider.getApplePlatformMember(request.getToken());

        return generateOAuthTokenResponse(
            Platform.APPLE,
            applePlatformMember.getEmail()
        );
    }

    private OAuthTokenResponse generateOAuthTokenResponse(Platform platform, String email) {
        return memberRepository.findByPlatformAndPlatformId(platform, email)
                .map(memberId -> {
                    Member findMember = memberRepository.findById(memberId)
                            .orElseThrow(NotFoundMemberException::new);
                    String accessToken = issueAccessToken(findMember);
                    String refreshToken = issueRefreshToken();

                    refreshTokenService.saveTokenInfo(findMember.getMemberId(), refreshToken, accessToken);

                    return new OAuthTokenResponse(accessToken, refreshToken, findMember.getEmail(), true);
                })
                .orElseGet(() -> {
                    Member oauthMember = new Member(email, platform);
                    Member savedMember = memberRepository.save(oauthMember);
                    String accessToken = issueAccessToken(savedMember);
                    String refreshToken = issueRefreshToken();

                    refreshTokenService.saveTokenInfo(savedMember.getMemberId(), refreshToken, accessToken);
                    return new OAuthTokenResponse(accessToken, refreshToken, email, false);
                });
    }

    private String issueAccessToken(final Member findMember) {
        return jwtTokenProvider.createAccessToken(findMember.getMemberId());
    }

    private String issueRefreshToken() {
        return Token.createRefreshToken();
    }

}
