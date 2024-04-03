package project.emergencyApplication.domain.member.entity;

import lombok.*;
import project.emergencyApplication.domain.base.BaseTime;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Table(name = "member", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email", "platform"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Member extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "name")
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "image")
    private byte image;

    @Column(name = "sub")
    private String sub; // Token Password 대체 사용

    @Enumerated(EnumType.STRING)
    @Column(name = "platform")
    private Platform platform;

    @Column(name = "watch_status")
    private boolean watchStatus;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    @Column(name = "connection_member")
    private List<ConnectionMember> connectionMembers;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    private Authority authority;

}