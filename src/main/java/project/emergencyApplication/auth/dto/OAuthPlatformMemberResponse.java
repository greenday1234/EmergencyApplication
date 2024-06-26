package project.emergencyApplication.auth.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.emergencyApplication.domain.member.entity.Authority;
import project.emergencyApplication.domain.member.entity.Location;
import project.emergencyApplication.domain.member.entity.Member;
import project.emergencyApplication.domain.member.entity.Platform;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OAuthPlatformMemberResponse {
    private String platformId;
    private String name;
    private String email;
    private String deviceToken;

    public Member createMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .name(name)
                .email(email)
                .deviceToken(deviceToken)
                .sub(passwordEncoder.encode(platformId))
                .authority(Authority.ROLE_USER)
                .platform(Platform.APPLE)
                .location(new Location(1.0, 1.0))
                .profileUrl(null)
                .build();
    }
}
