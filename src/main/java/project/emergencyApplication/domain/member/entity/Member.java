package project.emergencyApplication.domain.member.entity;

import lombok.*;
import project.emergencyApplication.domain.base.BaseTime;
import project.emergencyApplication.domain.member.dto.MemberUpdateRequestDto;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "member", uniqueConstraints = {@UniqueConstraint(columnNames = {"email", "platform"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Member extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "name")
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "profile_url")
    private String profileUrl;

    @Column(name = "device_token")  // FCM deviceToken
    private String deviceToken;

    @Column(name = "sub")
    private String sub; // Token Password 대체 사용

    @Enumerated(EnumType.STRING)
    @Column(name = "platform")
    private Platform platform;

    @Column(name = "emgState")
    private Boolean emgState;

    @Column(name = "watch_status")
    private Boolean hasWatch;

    @Embedded
    @Column(name = "location")
    private Location location;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    private Authority authority;

    public void updateMember(MemberUpdateRequestDto memberUpdateRequestDto) {
        this.name = memberUpdateRequestDto.getName();
        this.email = memberUpdateRequestDto.getEmail();
        this.hasWatch = memberUpdateRequestDto.isHasWatch();
    }

    public void updateMemberImage(String url) {
        this.profileUrl = url;
    }

    public void updateLocation(Double N, Double E) {
        this.location.setLocation(N, E);
    }

    public void updateEmgState(Boolean emgState) {
        this.emgState = emgState;
    }

}