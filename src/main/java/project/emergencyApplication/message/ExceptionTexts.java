package project.emergencyApplication.message;

import lombok.Getter;

@Getter
public enum ExceptionTexts {

    NOT_EXIST("해당 유저가 존재하지 않습니다."),
    NOT_EXIST_EMAIL("해당 이메일을 가진 유저가 존재하지 않습니다.");

    private String text;

    ExceptionTexts(String text) {
        this.text = text;
    }
}
