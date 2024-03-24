package project.emergencyApplication.auth.jwt.apple;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

/**
 * RSA 공개 키 생성
 * n, e 를 사용해 공개키를 생성한다.
 * 생성에 성공한다면 id_token의 claim 추출에 사용된다.
 */
@Component
public class ApplePublicKeyGenerator {

    private static final String SIGN_ALGORITHM_HEADER = "alg";
    private static final String KEY_ID_HEADER = "kid";
    private static final int POSITIVE_SIGN_NUMBER = 1;

    public PublicKey generatePublicKey(Map<String, String> headers, ApplePublicKeys publicKeys) {
        ApplePublicKey applePublicKey = publicKeys.getMatchingKey(
                headers.get(SIGN_ALGORITHM_HEADER),
                headers.get(KEY_ID_HEADER)
        );
        return generatePublicKeyWithApplePublicKey(applePublicKey);
    }

    private PublicKey generatePublicKeyWithApplePublicKey(ApplePublicKey applePublicKey) {
        byte[] nBytes = Base64.getUrlDecoder().decode(applePublicKey.getN());
        byte[] eBytes = Base64.getUrlDecoder().decode(applePublicKey.getE());

        BigInteger n = new BigInteger(POSITIVE_SIGN_NUMBER, nBytes);
        BigInteger e = new BigInteger(POSITIVE_SIGN_NUMBER, eBytes);
        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(n, e);

        try {
            KeyFactory keyFactory = KeyFactory.getInstance(applePublicKey.getKty());
            return keyFactory.generatePublic(rsaPublicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            throw new RuntimeException("Apple OAuth 로그인 중 public key 생성에 문제가 발생했습니다.");
        }
    }
}
