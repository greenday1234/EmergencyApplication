package project.emergencyApplication.auth.dto;

import io.jsonwebtoken.security.Password;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.emergencyApplication.domain.member.entity.Authority;
import project.emergencyApplication.domain.member.entity.Member;
import project.emergencyApplication.domain.member.entity.Platform;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OAuthPlatformMemberResponse {

    private String platformId;
    private String name;
    private String email;

    public Member createMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .name(name)
                .email(email)
                .sub(passwordEncoder.encode(platformId))
                .authority(Authority.ROLE_USER)
                .platform(Platform.APPLE)
                .build();
    }
}
