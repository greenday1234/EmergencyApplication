package project.emergencyApplication.domain.message.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.emergencyApplication.domain.base.BaseTime;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "messages")
public class Message extends BaseTime {

    @Id
    @GeneratedValue
    @Column(name = "message_id")
    private Long messageId;

    @Column(name = "receive_member_id")
    private Long receiveMemberId;   // 메시지를 받은 유저 ID

    @Column(name = "send_member_id")
    private Long sendMemberId;    // 메시지를 보낸 유저 ID

    @Column(name = "message")
    private String message;
}
