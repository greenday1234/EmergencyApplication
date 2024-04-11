package project.emergencyApplication.fcm.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class FCMConnectionNotificationRequestDto {

    private String title;
    private String body;
    private List<String> connectionEmails;
}
