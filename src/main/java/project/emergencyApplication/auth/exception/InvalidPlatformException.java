package project.emergencyApplication.auth.exception;

public class InvalidPlatformException extends RuntimeException {

    public InvalidPlatformException() {
        super("플랫폼 정보가 올바르지 않습니다.");
    }
}
