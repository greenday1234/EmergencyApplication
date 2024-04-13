package project.emergencyApplication.fcm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.emergencyApplication.fcm.dto.FCMConnectionNotificationRequestDto;
import project.emergencyApplication.fcm.dto.FCMConnectionReceiveRequestDto;
import project.emergencyApplication.fcm.dto.FCMNotificationRequestDto;
import project.emergencyApplication.fcm.service.FCMService;

@Tag(name = "fcm", description = "FCM 알림")
@RestController
@RequiredArgsConstructor
@RequestMapping("/fcm")
public class FCMController {

    private final FCMService fcmService;

    @Operation(summary = "여러 명에게 알림 보내기")
    @PostMapping()
    public String multipleSendNotificationByToken(@RequestBody FCMNotificationRequestDto requestDto) {
        return fcmService.multipleSendNotificationByToken(requestDto);
    }

    @Operation(summary = "계정 연동 알림")
    @PostMapping("/connection")
    public String sendConnectionNotification(@RequestBody FCMConnectionNotificationRequestDto requestDto) {
        return fcmService.sendConnectionNotification(requestDto);
    }

    @Operation(summary = "계정 연동 요청 수락 및 거절")
    @PostMapping("/connection/receive")
    public String receiveConnectionNotification(@RequestBody FCMConnectionReceiveRequestDto requestDto) {
        return fcmService.receiveConnectionNotification(requestDto);
    }
}
