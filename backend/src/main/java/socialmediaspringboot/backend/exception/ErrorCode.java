package socialmediaspringboot.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(1999, "Uncategorized error.",
            HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_MESSAGE_KEY(1998, "Uncategorized error.",
            HttpStatus.BAD_REQUEST),
    USERNAME_EXISTED(101, "This username has been used.",
            HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(102, "User not found.",
            HttpStatus.NOT_FOUND),
    INVALID_USERNAME(103, "Username must be at least 6 characters.",
            HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(104,
            "Password must be from 6 to 50 characters, contains at least 1 digit and 1 uppercase letter",
            HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(105, "Unauthenticated.",
            HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED_ACCESS(106, "You do not have the permission to access this page.",
            HttpStatus.FORBIDDEN)
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode){
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
