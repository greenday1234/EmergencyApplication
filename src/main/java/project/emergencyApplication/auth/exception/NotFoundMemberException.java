package project.emergencyApplication.auth.exception;

import lombok.Getter;
import org.webjars.NotFoundException;

@Getter
public class NotFoundMemberException extends NotFoundException {

    public NotFoundMemberException() {
        super("존재하지 않는 회원입니다.");
    }
}
