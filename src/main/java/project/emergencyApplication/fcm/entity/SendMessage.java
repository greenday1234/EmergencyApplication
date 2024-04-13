package project.emergencyApplication.fcm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "send_message")
@AllArgsConstructor
@Builder
public class SendMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "send_message_id")
    private Long sendMessageId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "message")
    private String message;
}
