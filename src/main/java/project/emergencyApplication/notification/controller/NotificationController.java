package project.emergencyApplication.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.emergencyApplication.notification.dto.ConnectionNotificationRequestDto;
import project.emergencyApplication.notification.dto.NotificationRequestDto;
import project.emergencyApplication.notification.service.NotificationService;

@Tag(name = "fcm", description = "FCM 알림")
@RestController
@RequiredArgsConstructor
@RequestMapping("/fcm")
public class NotificationController {

    private final NotificationService fcmService;

    @Operation(summary = "여러 명에게 알림 보내기")
    @PostMapping()
    public String multipleSendNotificationByToken(@RequestBody NotificationRequestDto requestDto) {
        return fcmService.multipleSendNotificationByToken(requestDto);
    }

    /** NOTE
     * 계정 연동 요청 API 를 따로 만들어야 함
     * 이메일, 이름, 사진이 들어간 Dto
     * 계정 연동 요청 메시지는 따로 생성하지 않음
     */
    @Operation(summary = "계정 연동 알림")
    @PostMapping("/connection")
    public String sendConnectionNotification(@RequestBody ConnectionNotificationRequestDto requestDto) {
        return fcmService.connectionNotification(requestDto);
    }

}
