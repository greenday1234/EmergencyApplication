package project.emergencyApplication.fcm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.emergencyApplication.fcm.dto.FCMNotificationRequestDto;
import project.emergencyApplication.fcm.service.FCMService;

@Tag(name = "fcm", description = "FCM 알림")
@RestController
@RequiredArgsConstructor
@RequestMapping("/fcm")
public class FCMController {

    private final FCMService fcmService;

    @Operation(summary = "알림 보내기")
    @PostMapping()
    public String sendNotificationByToken(@RequestBody FCMNotificationRequestDto requestDto) {
        return fcmService.sendNotificationByToken(requestDto);
    }
}
