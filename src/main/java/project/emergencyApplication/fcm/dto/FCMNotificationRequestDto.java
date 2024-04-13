package project.emergencyApplication.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.emergencyApplication.auth.jwt.utils.SecurityUtil;
import project.emergencyApplication.fcm.entity.ReceiveMessage;
import project.emergencyApplication.fcm.entity.SendMessage;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FCMNotificationRequestDto {

    private String title;
    private String body;

    public SendMessage createSendNotificationMessage() {
          return SendMessage.builder()
                .memberId(SecurityUtil.getCurrentMemberId())
                .message(body)
                .build();
    }

    public ReceiveMessage createReceiveNotificationMessage(Long connMemberId) {
        return ReceiveMessage.builder()
                .memberId(connMemberId)
                .senderMemberId(SecurityUtil.getCurrentMemberId())
                .message(body)
                .build();
    }
}
