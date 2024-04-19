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

    @Column(name = "image")
    private byte image;

    @Column(name = "device_token")  // FCM deviceToken
    private String deviceToken;

    @Column(name = "sub")
    private String sub; // Token Password 대체 사용

    @Enumerated(EnumType.STRING)
    @Column(name = "platform")
    private Platform platform;

    @Column(name = "watch_status")
    private boolean hasWatch;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    private Authority authority;

    public void updateMember(MemberUpdateRequestDto memberUpdateRequestDto) {
        this.name = memberUpdateRequestDto.getName();
        this.email = memberUpdateRequestDto.getEmail();
        this.hasWatch = memberUpdateRequestDto.isHasWatch();
        this.image = memberUpdateRequestDto.getImage();
    }

}