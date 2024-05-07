package project.emergencyApplication.message;

import lombok.Getter;

@Getter
public enum ImageTexts {

    IMAGE_DELETE_SUCCESS("이미지 삭제에 성공했습니다.");

    private String text;

    ImageTexts(String text) {
        this.text = text;
    }
}
