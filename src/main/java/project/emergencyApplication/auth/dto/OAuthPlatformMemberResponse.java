package project.emergencyApplication.auth.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    public Member createMember(Platform platform, Authority authority) {
        return Member.builder()
                .name(name)
                .email(email)
                .authority(authority)
                .platform(platform)
                .build();
    }
}
