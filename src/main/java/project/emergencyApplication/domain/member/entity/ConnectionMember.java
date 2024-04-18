package project.emergencyApplication.domain.member.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "connection_member")
public class ConnectionMember {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "connection_member_id")
    private Long connectionMemberId;

    @Column(name = "connection_id")
    private Long connectionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static ConnectionMember createConnectionMember(Member connMember) {
        return ConnectionMember.builder()
                .connectionId(connMember.getMemberId())
                .build();
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
