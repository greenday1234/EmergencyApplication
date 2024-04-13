package project.emergencyApplication.fcm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.emergencyApplication.domain.member.entity.Member;

import javax.persistence.*;

@Entity
@Table(name = "receive_message")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReceiveMessage {

    @Id
    @GeneratedValue
    @Column(name = "receive_message_id")
    private Long receiveMessageId;

    @Column(name = "message_member_id")
    private Long messageMemberId;

    @Column(name = "sender_id")
    private Long senderMemberId;    // 메시지를 보낸 유저 ID

    @Column(name = "message")
    private String message;


}
