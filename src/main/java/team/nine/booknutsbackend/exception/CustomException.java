package team.nine.booknutsbackend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import team.nine.booknutsbackend.dto.response.ErrorResponse;

@Getter
public class CustomException extends RuntimeException {

    private final HttpStatus status;
    private final ErrorResponse errorResponse;

    protected CustomException(HttpStatus httpStatus, String errName, String message) {
        this.status = httpStatus;
        this.errorResponse = ErrorResponse.error(httpStatus.value(), errName, message);
    }

}