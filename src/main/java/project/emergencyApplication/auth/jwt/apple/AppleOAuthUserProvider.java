package project.emergencyApplication.auth.jwt.apple;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.emergencyApplication.auth.dto.OAuthPlatformMemberResponse;
import project.emergencyApplication.auth.exception.InvalidAccessTokenException;

import java.security.PublicKey;
import java.util.Map;

/**
 * Apple 계정 유저 정보 반환
 */
@Component
@RequiredArgsConstructor
public class AppleOAuthUserProvider {

    private final AppleTokenParser appleJwtParser;
    private final AppleClient appleClient;
    private final ApplePublicKeyGenerator publicKeyGenerator;
    private final AppleClaimsValidator appleClaimsValidator;

    public OAuthPlatformMemberResponse getApplePlatformMember(String identityToken) {
        Map<String, String> headers = appleJwtParser.parseHeaders(identityToken);
        ApplePublicKeys applePublicKeys = appleClient.getApplePublicKeys();

        PublicKey publicKey = publicKeyGenerator.generatePublicKey(headers, applePublicKeys);

        Claims claims = appleJwtParser.parsePublicKeyAndGetClaims(identityToken, publicKey);
        System.out.println("claioms>>>" + claims);
        validateClaims(claims);
        return new OAuthPlatformMemberResponse(claims.getSubject(), claims.get("email", String.class));
    }

    private void validateClaims(Claims claims) {
        if (!appleClaimsValidator.isValid(claims)) {
            throw new InvalidAccessTokenException("Apple OAuth Claims 값이 올바르지 않습니다.");
        }
    }
}
