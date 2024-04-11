package project.emergencyApplication.domain.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "connection_member")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionMember {

    @Id
    @Column(name = "connection_member_id")
    private Long connectionMemberId;  // 연동된 계정의 MemberId

    @Column(name = "connection_member_name")
    private String connectionMemberName;

    @Column(name = "connection_member_image")
    private byte connectionMemberImage;

    @Column(name = "connection_member_email")
    private String connectionMemberEmail;

    @Column(name = "connection_member_devicetoken")
    private String connectionMemberDeviceToken;

    // 회원 관계 매핑 (연관관계 주인)
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
