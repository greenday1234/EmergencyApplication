package project.emergencyApplication.auth.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class OAuthTokenResponse {
    private String accessToken;
    private String refreshToken;
    private String email;
    private Boolean isRegistered;
}