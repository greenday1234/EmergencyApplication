package project.emergencyApplication.auth.exception;

public class InvalidAccessTokenException extends RuntimeException {

    public InvalidAccessTokenException() {
        super("올바르지 않은 Access Token 입니다. 다시 로그인해주세요.");
    }

    public InvalidAccessTokenException(String message) {
        super(message);
    }
}
