package project.emergencyApplication.message;

import lombok.Getter;

@Getter
public enum ExceptionTexts {

    NOT_EXIST("해당 유저가 존재하지 않습니다."),
    NOT_EXIST_EMAIL("해당 이메일을 가진 유저가 존재하지 않습니다."),
    EMPTY_FILE_EXCEPTION("파일이 비어있습니다."),
    IO_EXCEPTION_ON_IMAGE_UPLOAD("이미지 파일을 업로드하는데 문제가 발생했습니다."),
    NO_FILE_EXTENTION("파일을 찾을 수 없습니다."),
    INVALID_FILE_EXTENTION("해당 파일의 확장자를 지원하지 않습니다."),
    PUT_OBJECT_EXCEPTION("오브젝트 에러"),
    IO_EXCEPTION_ON_IMAGE_DELETE("이미지를 삭제하는데에 문제가 발생했습니다.");

    private String text;

    ExceptionTexts(String text) {
        this.text = text;
    }
}
