package socialmediaspringboot.backend.exception;

import lombok.Getter;
import lombok.Setter;

/**
 This class extends RuntimeException and allows for centralized error code management,
 * providing a consistent way to throw and handle application-specific exceptions.
 */

@Getter
@Setter
public class AppException extends RuntimeException {

    private ErrorCode errorCode;

    public AppException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
