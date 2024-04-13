package project.emergencyApplication.fcm.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import project.emergencyApplication.auth.jwt.utils.SecurityUtil;
import project.emergencyApplication.fcm.entity.SendMessage;

@Getter
@NoArgsConstructor
public class FCMConnectionNotificationRequestDto {

    private String title;
    private String body;
    private String connectionEmail;

    public SendMessage createSendConnMessage(FCMConnectionNotificationRequestDto requestDto) {
        SendMessage sendMessage = SendMessage.builder()
                .memberId(SecurityUtil.getCurrentMemberId())
                .message(requestDto.getBody())
                .build();

        return sendMessage;
    }
}
