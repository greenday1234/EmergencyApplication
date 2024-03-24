package project.emergencyApplication.auth.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class AppleLoginRequest {

    @NotBlank(message = "공백일 수 없습니다.")
    private String token;
}
