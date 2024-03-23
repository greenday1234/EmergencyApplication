package project.emergencyApplication.auth.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * JWK 리스트를 받아오면 이 안에 id_token과 일치하는 kid와 alg 값이 존재한다.
 */
@Getter
@NoArgsConstructor
public class ApplePublicKeys {

    private List<ApplePublicKey> keys;

    public ApplePublicKeys(List<ApplePublicKey> keys) {
        this.keys = List.copyOf(keys);
    }

    public ApplePublicKey getMatchingKey(final String alg, final String kid) {
        return keys.stream()
                .filter(key -> key.isSameAlg(alg) && key.isSameKid(kid))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("잘못된 토큰 형태입니다."));
    }
}
