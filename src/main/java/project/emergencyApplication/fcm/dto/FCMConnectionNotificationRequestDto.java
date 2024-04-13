package project.emergencyApplication.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.emergencyApplication.auth.jwt.utils.SecurityUtil;
import project.emergencyApplication.domain.member.entity.Member;
import project.emergencyApplication.fcm.entity.Connection;
import project.emergencyApplication.fcm.entity.ReceiveMessage;
import project.emergencyApplication.fcm.entity.SendMessage;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FCMConnectionNotificationRequestDto {

    private String title;
    private String body;
    private String connectionEmail;

    public SendMessage createSendConnMessage() {
        SendMessage sendMessage = SendMessage.builder()
                .memberId(SecurityUtil.getCurrentMemberId())
                .message(body)
                .build();

        return sendMessage;
    }

    public Connection createSendConn(Member findConnMember) {
        return Connection.builder()
                .sendConnectionId(SecurityUtil.getCurrentMemberId())
                .receiveConnectionId(findConnMember.getMemberId())
                .sendBool(true)
                .build();
    }

    public ReceiveMessage createReceiveMessage(Member findConnMember) {
        return ReceiveMessage.builder()
                .memberId(findConnMember.getMemberId())
                .senderMemberId(SecurityUtil.getCurrentMemberId())
                .message(body)
                .build();
    }
}
