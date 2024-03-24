package project.emergencyApplication.auth.jwt.apple;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * 여기서 사용되는 값은 kty, n, e 3가지
 * n, e 값은 RSA의 공개키 생성에 사용, kty는 RSA란 값으로 고정돼있다.
 */
@Getter
public class ApplePublicKey {

    private String kty;
    private String kid;
    private String use;
    private String alg;
    private String n;
    private String e;

    public boolean isSameAlg(final String alg) {
        return this.alg.equals(alg);
    }
    public boolean isSameKid(final String kid) {
        return this.kid.equals(kid);
    }
}
