package project.emergencyApplication.image.exception;

import org.webjars.NotFoundException;
import project.emergencyApplication.texts.ExceptionTexts;

public class S3Exception extends NotFoundException {

    public S3Exception(ExceptionTexts exceptionTexts) {
        super(exceptionTexts.getText());
    }
}
