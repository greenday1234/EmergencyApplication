package project.emergencyApplication.image.exception;

import org.webjars.NotFoundException;
import project.emergencyApplication.message.ExceptionTexts;

public class S3Exception extends NotFoundException {

    public S3Exception(ExceptionTexts exceptionTexts) {
        super(exceptionTexts.getText());
    }
}
