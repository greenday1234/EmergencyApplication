package project.emergencyApplication.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.emergencyApplication.auth.jwt.utils.SecurityUtil;
import project.emergencyApplication.fcm.entity.Messages;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FCMConnectionReceiveRequestDto {

    private String sendMemberEmail; // 요청을 보낸 유저
    private Boolean state;
    private String title;
    private String body;

    public Messages createConnReceiveMessage(Long memberId) {
        return Messages.builder()
                .receiveMemberId(memberId)
                .senderMemberId(SecurityUtil.getCurrentMemberId())
                .message(body)
                .build();
    }
}
