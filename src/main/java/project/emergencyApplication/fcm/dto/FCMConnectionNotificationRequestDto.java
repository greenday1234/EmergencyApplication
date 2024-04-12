package project.emergencyApplication.fcm.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FCMConnectionNotificationRequestDto {

    private String title;
    private String body;
    private String connectionEmail;
}
