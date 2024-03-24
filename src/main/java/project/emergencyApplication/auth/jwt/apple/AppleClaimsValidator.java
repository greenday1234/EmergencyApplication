package project.emergencyApplication.auth.jwt.apple;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import project.emergencyApplication.auth.jwt.utils.EncryptUtils;

/**
 * Apple 에서 발급한 id_token이 맞는지 확인하기 위한 iss 검증 (값에 https://appleid.apple.com 가 포함되어 있는지만 확인하면 된다.)
 * aud 값은 앱 등록시 생성된 Bundle ID 값 (ClientId)
 * nonce 값은 클라이언트 측에서 CSRF 공격을 방지하기 위해 생성한 임의의 문자열
 */
@Component
public class AppleClaimsValidator {

    private static final String NONCE_KEY = "nonce";

    private final String iss;
    private final String clientId;
    private final String nonce;

    public AppleClaimsValidator(
            @Value("${oauth.apple.iss}") String iss,
            @Value("${oauth.apple.client-id}") String clientId,
            @Value("${oauth.apple.nonce}") String nonce
    ) {
        this.iss = iss;
        this.clientId = clientId;
        this.nonce = EncryptUtils.encrypt(nonce);
    }

    public boolean isValid(Claims claims) {
        return claims.getIssuer().contains(iss) &&
                claims.getAudience().equals(clientId) &&
                claims.get(NONCE_KEY, String.class).equals(nonce);
    }
}
