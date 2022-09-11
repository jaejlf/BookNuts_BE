package team.nine.booknutsbackend.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.nine.booknutsbackend.dto.response.ErrorResponse;
import team.nine.booknutsbackend.exception.debate.CannotEnterException;
import team.nine.booknutsbackend.exception.s3.UploadFailedException;
import team.nine.booknutsbackend.exception.user.ExpiredAccessTokenException;
import team.nine.booknutsbackend.exception.user.ExpiredRefreshTokenException;
import team.nine.booknutsbackend.exception.user.InvalidTokenException;
import team.nine.booknutsbackend.exception.user.NoAuthException;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private void errorLogging(Exception e) {
        log.error("[" + e.getClass().getSimpleName() + "] " + e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> defaultExceptionHandler(Exception e) {
        errorLogging(e);
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.error(INTERNAL_SERVER_ERROR.value(), e.getClass().getSimpleName(), e.getMessage()));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> customExceptionHandler(CustomException e) {
        errorLogging(e);
        return ResponseEntity
                .status(e.getStatus())
                .body(e.getErrorResponse());
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<Object> entityExistsExceptionHandler(EntityExistsException e) {
        errorLogging(e);
        return ResponseEntity
                .status(CONFLICT)
                .body(ErrorResponse.error(CONFLICT.value(), e.getClass().getSimpleName(), e.getMessage()));
    }

    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ResponseEntity<Object> indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException e) {
        errorLogging(e);
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ErrorResponse.error(BAD_REQUEST.value(), e.getClass().getSimpleName(), e.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> usernameNotFoundExceptionHandler(UsernameNotFoundException e) {
        errorLogging(e);
        return ResponseEntity
                .status(NOT_FOUND)
                .body(ErrorResponse.error(NOT_FOUND.value(), e.getClass().getSimpleName(), e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        errorLogging(e);
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ErrorResponse.error(BAD_REQUEST.value(), e.getClass().getSimpleName(), e.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> entityNotFoundExceptionHandler(EntityNotFoundException e) {
        errorLogging(e);
        return ResponseEntity
                .status(NOT_FOUND)
                .body(ErrorResponse.error(NOT_FOUND.value(), e.getClass().getSimpleName(), e.getMessage()));
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Object> emptyResultDataAccessExceptionHandler(EmptyResultDataAccessException e) {
        errorLogging(e);
        return ResponseEntity
                .status(NOT_FOUND)
                .body(ErrorResponse.error(NOT_FOUND.value(), e.getClass().getSimpleName(), e.getMessage()));
    }


    @ExceptionHandler(CannotEnterException.class)
    public ResponseEntity<Object> handleCannotEnterException(CannotEnterException e) {
        errorLogging(e);
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ErrorResponse.error(BAD_REQUEST.value(), e.getClass().getSimpleName(), e.getMessage()));
    }

    @ExceptionHandler(ExpiredRefreshTokenException.class)
    public ResponseEntity<Object> handleExpiredRefreshTokenException(ExpiredRefreshTokenException e) {
        errorLogging(e);
        return ResponseEntity
                .status(FORBIDDEN)
                .body(ErrorResponse.error(FORBIDDEN.value(), e.getClass().getSimpleName(), e.getMessage()));
    }

    @ExceptionHandler(ExpiredAccessTokenException.class)
    public ResponseEntity<Object> handleExpiredTokenException(ExpiredAccessTokenException e) {
        errorLogging(e);
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(ErrorResponse.error(UNAUTHORIZED.value(), e.getClass().getSimpleName(), e.getMessage()));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Object> handleInvalidTokenException(InvalidTokenException e) {
        errorLogging(e);
        return ResponseEntity
                .status(FORBIDDEN)
                .body(ErrorResponse.error(FORBIDDEN.value(), e.getClass().getSimpleName(), e.getMessage()));
    }

    @ExceptionHandler(NoAuthException.class)
    public ResponseEntity<Object> handleNoAuthException(NoAuthException e) {
        errorLogging(e);
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ErrorResponse.error(BAD_REQUEST.value(), e.getClass().getSimpleName(), e.getMessage()));
    }

    @ExceptionHandler(UploadFailedException.class)
    public ResponseEntity<Object> handleUploadFailedException(UploadFailedException e) {
        errorLogging(e);
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.error(INTERNAL_SERVER_ERROR.value(), e.getClass().getSimpleName(), e.getMessage()));
    }

}
