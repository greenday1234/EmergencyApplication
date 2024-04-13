package project.emergencyApplication.fcm.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import project.emergencyApplication.auth.jwt.utils.SecurityUtil;
import project.emergencyApplication.fcm.entity.SendMessage;

@Getter
@NoArgsConstructor
public class FCMNotificationRequestDto {

    private String title;
    private String body;

    public SendMessage createSendNotificationMessage(FCMNotificationRequestDto requestDto) {
        SendMessage sendMessage = SendMessage.builder()
                .memberId(SecurityUtil.getCurrentMemberId())
                .message(requestDto.getBody())
                .build();

        return sendMessage;
    }
}
