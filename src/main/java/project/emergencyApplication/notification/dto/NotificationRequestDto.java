package project.emergencyApplication.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.emergencyApplication.auth.jwt.utils.SecurityUtil;
import project.emergencyApplication.domain.message.entity.Message;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDto {

    private String title;
    private String body;

    public Message createNotificationMessage(Long memberId) {
          return Message.builder()
                .receiveMemberId(memberId)
                .sendMemberId(SecurityUtil.getCurrentMemberId())
                .message(body)
                .build();
    }
}
