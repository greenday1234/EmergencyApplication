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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "connection_member_id")
    private Long connectionMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member connectionMember; //상대방

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member member; //나

    public ConnectionMember createConnectionMember(Member member, Member connectionMember) {
        return ConnectionMember.builder()
                .connectionMember(connectionMember)
                .member(member)
                .build();
    }
}
