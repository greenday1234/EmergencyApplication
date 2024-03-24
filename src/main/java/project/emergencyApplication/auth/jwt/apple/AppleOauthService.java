package project.emergencyApplication.auth.jwt.apple;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AppleOauthService {

    private final AppleTokenParser appleTokenParser;
    private final AppleClient appleClient;
    private final ApplePublicKeyGenerator applePublicKeyGenerator;

    private final String DEFAULT_NAME = "apple";
    private final String CLAIM_EMAIL = "email";

    public AppleUser createAppleUser(final String appleToken) {
        final Map<String, String> appleTokenHeader = appleTokenParser.parseHeaders(appleToken);
        final ApplePublicKeys applePublicKeys = appleClient.getApplePublicKeys();
        final PublicKey publicKey = applePublicKeyGenerator.generatePublicKey(appleTokenHeader, applePublicKeys);
        final Claims claims = appleTokenParser.parsePublicKeyAndGetClaims(appleToken, publicKey);
        return new AppleUser(DEFAULT_NAME, claims.get(CLAIM_EMAIL, String.class), claims.getSubject());
    }
}
