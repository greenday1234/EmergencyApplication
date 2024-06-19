package project.emergencyApplication.auth.exception;

public class RefreshTokenExpireException extends RuntimeException {

    public RefreshTokenExpireException(String message) {
        super(message);
    }
}
