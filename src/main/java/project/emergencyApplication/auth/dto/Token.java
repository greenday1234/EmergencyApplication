package project.emergencyApplication.auth.dto;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token {

    @Id
    @Column(name = "token_id")
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
