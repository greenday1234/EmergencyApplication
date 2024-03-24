package project.emergencyApplication.auth.dto;

import lombok.*;

import javax.persistence.Id;
import java.util.UUID;

//entity 할지말지는 고민하자.
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token {

    @Id //memberId
    private Long id;
    private String refreshToken;
    private String accessToken;
    private Long expiration;

    public void setAccessToken(String newAccessToken) {
        this.accessToken = newAccessToken;
    }

    public static String createRefreshToken() {
        return UUID.randomUUID().toString();
    }
}
