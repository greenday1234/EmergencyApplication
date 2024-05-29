package project.emergencyApplication.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.emergencyApplication.auth.jwt.utils.SecurityUtil;
import project.emergencyApplication.domain.member.entity.Member;
import project.emergencyApplication.fcm.entity.Connection;
import project.emergencyApplication.domain.message.entity.Message;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FCMConnectionNotificationRequestDto {

    private String title;
    private String body;
    private String connectionEmail;
    private Boolean state;
    private Boolean firstRequest;   // 처음 계정 연동을 요청하는 경우 true (요청에 응답하는 경우엔 false)

    public Message createConnMessage(Long memberId) {
        return Message.builder()
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
