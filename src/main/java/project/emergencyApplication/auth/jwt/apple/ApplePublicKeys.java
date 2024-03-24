package project.emergencyApplication.auth.jwt.apple;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * JWK 리스트를 받아오면 이 안에 id_token과 일치하는 kid와 alg 값이 존재한다.
 * 일종의 DTO 클래스이므로 필드명을 keys가 아닌 다른 값으로 하면 응답을 받아오지 못하는 점 주의!
 */
@Getter
@NoArgsConstructor
public class ApplePublicKeys {

    private List<ApplePublicKey> keys;

    public ApplePublicKey getMatchingKey(String alg, String kid) {
        return keys.stream()
                .filter(key -> key.isSameAlg(alg) && key.isSameKid(kid))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("잘못된 토큰 형태입니다."));
    }
}
