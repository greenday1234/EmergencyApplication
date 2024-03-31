package project.emergencyApplication.domain.member.entity;

import lombok.*;
import project.emergencyApplication.domain.base.BaseTime;

import javax.persistence.*;

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

    @Column(name = "sub")
    private String sub;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform")
    private Platform platform;

    @Column(name = "watch_status")
    private boolean watchStatus;

    @Column(name = "connection_email")
    private String connectionEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    private Authority authority;

}