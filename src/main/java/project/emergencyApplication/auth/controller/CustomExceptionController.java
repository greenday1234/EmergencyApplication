package project.emergencyApplication.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import project.emergencyApplication.auth.exception.RefreshTokenExpireException;

@ControllerAdvice
public class CustomExceptionController {

        @ExceptionHandler(RefreshTokenExpireException.class)
        public final ResponseEntity<String> handleCustomException(RefreshTokenExpireException ex) {

            // 403 상태 코드로 응답
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
        }
}
