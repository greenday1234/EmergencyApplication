package project.emergencyApplication.auth.jwt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * id_token 검증
 * 애플 서버에 JWK 리스트를 받아온다. 이를 일급 컬렉션 형태로 관리한다.
 * 여기서 사용되는 값은 kty, n, e 3가지이고
 * n, e 값은 RSA의 공개키 생성에 사용, kty는 RSA란 값으로 고정돼있다.
 */
@Getter
public class ApplePublicKey {

    private final String kty;

    private final String kid;

    private final String use;

    private final String alg;

    private final String n;

    private final String e;

    public boolean isSameAlg(final String alg) {
        return this.alg.equals(alg);
    }

    public boolean isSameKid(final String kid) {
        return this.kid.equals(kid);
    }

    @JsonCreator
    public ApplePublicKey(@JsonProperty("kty") final String kty,
                          @JsonProperty("kid") final String kid,
                          @JsonProperty("use") final String use,
                          @JsonProperty("alg") final String alg,
                          @JsonProperty("n") final String n,
                          @JsonProperty("e") final String e) {
        this.kty = kty;
        this.kid = kid;
        this.use = use;
        this.alg = alg;
        this.n = n;
        this.e = e;
    }
}
