package project.emergencyApplication.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.emergencyApplication.auth.jwt.utils.SecurityUtil;
import project.emergencyApplication.domain.member.entity.Member;
import project.emergencyApplication.fcm.entity.Connection;
import project.emergencyApplication.fcm.entity.Messages;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FCMConnectionNotificationRequestDto {

    private String title;
    private String body;
    private String connectionEmail;
    private Boolean state;

    public Messages createConnMessage(Long memberId) {
        return Messages.builder()
                .receiveMemberId(memberId)
                .senderMemberId(SecurityUtil.getCurrentMemberId())
                .message(body)
                .build();
    }

    public Connection createSendConn(Member findConnMember) {
        return Connection.builder()
                .sendConnectionId(SecurityUtil.getCurrentMemberId())
                .receiveConnectionId(findConnMember.getMemberId())
                .sendBool(state)
                .build();
    }
}
