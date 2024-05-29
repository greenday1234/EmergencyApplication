package project.emergencyApplication.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.emergencyApplication.auth.jwt.utils.SecurityUtil;
import project.emergencyApplication.domain.message.entity.Messages;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FCMNotificationRequestDto {

    private String title;
    private String body;

    public Messages createNotificationMessage(Long memberId) {
          return Messages.builder()
                .receiveMemberId(memberId)
                .senderMemberId(SecurityUtil.getCurrentMemberId())
                .message(body)
                .build();
    }
}
