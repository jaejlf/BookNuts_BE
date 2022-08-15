package team.nine.booknutsbackend.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.nine.booknutsbackend.exception.archive.ArchiveDuplicateException;
import team.nine.booknutsbackend.exception.archive.ArchiveNotFoundException;
import team.nine.booknutsbackend.exception.board.BoardNotFoundException;
import team.nine.booknutsbackend.exception.board.OutOfIndexException;
import team.nine.booknutsbackend.exception.comment.CommentNotFoundException;
import team.nine.booknutsbackend.exception.comment.NotNewCommentCreateException;
import team.nine.booknutsbackend.exception.debate.CannotEnterException;
import team.nine.booknutsbackend.exception.debate.DebateUserNotFoundException;
import team.nine.booknutsbackend.exception.debate.RoomNotFoundException;
import team.nine.booknutsbackend.exception.debate.StatusChangeException;
import team.nine.booknutsbackend.exception.follow.AlreadyFollowingException;
import team.nine.booknutsbackend.exception.follow.CannotFollowException;
import team.nine.booknutsbackend.exception.follow.NotFollowingException;
import team.nine.booknutsbackend.exception.s3.UploadFailedException;
import team.nine.booknutsbackend.exception.series.SeriesDuplicateException;
import team.nine.booknutsbackend.exception.series.SeriesNotFoundException;
import team.nine.booknutsbackend.exception.user.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 400 Bad Request            요청 형식이 틀렸을 경우
 * 401 Unauthorized	          리소스 접근 권한이 없는 경우
 * 403 Forbidden	          해당 리소스에 접근하는 것이 허락되지 않을 경우
 * 404 Not Found	          요청한 리소스가 존재하지 않을 경우
 * 405 Method Not Allowed	  요청을 처리할 메소드가 없는 경우
 * 409 Conflict               서버의 현재 상태와 요청이 충돌한 경우
 * 500 Internal Server Error  서버에서 에러가 발생한 경우
 * 503 Service Unavailable	  현재 이용할 수 없는 서비스
 **/

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    private Map<String, String> getExceptionDescription(Exception e) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("error", e.getClass().getSimpleName());
        map.put("msg", e.getMessage());
        log.error("[" + e.getClass().getSimpleName() + "] " + e.getMessage());
        return map;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception e) {
        return new ResponseEntity<>(getExceptionDescription(e), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ArchiveDuplicateException.class)
    public ResponseEntity<Object> handleArchiveDuplicateException(ArchiveDuplicateException e) {
        return new ResponseEntity<>(getExceptionDescription(e), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ArchiveNotFoundException.class)
    public ResponseEntity<Object> handleArchiveNotFoundException(ArchiveNotFoundException e) {
        return new ResponseEntity<>(getExceptionDescription(e), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity<Object> handleBoardNotFoundException(BoardNotFoundException e) {
        return new ResponseEntity<>(getExceptionDescription(e), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OutOfIndexException.class)
    public ResponseEntity<Object> handleOutOfIndexException(OutOfIndexException e) {
        return new ResponseEntity<>(getExceptionDescription(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CannotEnterException.class)
    public ResponseEntity<Object> handleCannotEnterException(CannotEnterException e) {
        return new ResponseEntity<>(getExceptionDescription(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DebateUserNotFoundException.class)
    public ResponseEntity<Object> handleDebateUserNotFoundException(DebateUserNotFoundException e) {
        return new ResponseEntity<>(getExceptionDescription(e), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<Object> handleRoomNotFoundException(RoomNotFoundException e) {
        return new ResponseEntity<>(getExceptionDescription(e), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StatusChangeException.class)
    public ResponseEntity<Object> handleStatusChangeException(StatusChangeException e) {
        return new ResponseEntity<>(getExceptionDescription(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyFollowingException.class)
    public ResponseEntity<Object> handleAlreadyFollowingException(AlreadyFollowingException e) {
        return new ResponseEntity<>(getExceptionDescription(e), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CannotFollowException.class)
    public ResponseEntity<Object> handleCannotFollowException(CannotFollowException e) {
        return new ResponseEntity<>(getExceptionDescription(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFollowingException.class)
    public ResponseEntity<Object> handleNotFollowingException(NotFollowingException e) {
        return new ResponseEntity<>(getExceptionDescription(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SeriesDuplicateException.class)
    public ResponseEntity<Object> handleSeriesDuplicateException(SeriesDuplicateException e) {
        return new ResponseEntity<>(getExceptionDescription(e), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SeriesNotFoundException.class)
    public ResponseEntity<Object> handleSeriesNotFoundException(SeriesNotFoundException e) {
        return new ResponseEntity<>(getExceptionDescription(e), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExpiredRefreshTokenException.class)
    public ResponseEntity<Object> handleExpiredRefreshTokenException(ExpiredRefreshTokenException e) {
        return new ResponseEntity<>(getExceptionDescription(e), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<Object> handleExpiredTokenException(ExpiredTokenException e) {
        return new ResponseEntity<>(getExceptionDescription(e), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Object> handleInvalidTokenException(InvalidTokenException e) {
        return new ResponseEntity<>(getExceptionDescription(e), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoAuthException.class)
    public ResponseEntity<Object> handleNoAuthException(NoAuthException e) {
        return new ResponseEntity<>(getExceptionDescription(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordErrorException.class)
    public ResponseEntity<Object> handlePasswordErrorException(PasswordErrorException e) {
        return new ResponseEntity<>(getExceptionDescription(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(getExceptionDescription(e), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UploadFailedException.class)
    public ResponseEntity<Object> handleUploadFailedException(UploadFailedException e) {
        return new ResponseEntity<>(getExceptionDescription(e), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotNewCommentCreateException.class)
    public ResponseEntity<Object> handleStatusChangeException(NotNewCommentCreateException e) {
        return new ResponseEntity<>(getExceptionDescription(e), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<Object> handleStatusChangeException(CommentNotFoundException e) {
        return new ResponseEntity<>(getExceptionDescription(e), HttpStatus.NOT_FOUND);
    }

}
