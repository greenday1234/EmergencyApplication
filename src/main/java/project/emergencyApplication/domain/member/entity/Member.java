package project.emergencyApplication.domain.member.entity;

import lombok.*;
import project.emergencyApplication.auth.dto.Token;
import project.emergencyApplication.domain.base.BaseTime;
import project.emergencyApplication.domain.platform.Platform;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "member", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email", "platform"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "name")
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform")
    private Platform platform;

    @Column(name = "watch_status")
    private boolean watchStatus;

    @Column(name = "connection_email")
    private String connectionEmail;

    @Column(name = "token")
    @Embedded
    private Token token;

    public Member(String  email, Platform platform) {
        this.email = email;
        this.platform = platform;
    }

    public void tokenUpdate(Token token) {
        this.token = token;
    }


}
