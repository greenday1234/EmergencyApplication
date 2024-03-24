package project.emergencyApplication.auth.dto;

import lombok.*;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token {

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
