package project.emergencyApplication.message;

import lombok.Getter;

@Getter
public enum MessageTexts {
    SUCCESS("알림을 성공적으로 전송했습니다."),
    FAIL("알림 보내기를 실패했습니다."),
    EMPTY_DEVICETOKEN("서버에 저장된 유저의 DeviceToken 이 존재하지 않습니다.");

    private String text;

    MessageTexts(String text) {
        this.text = text;
    }

}
