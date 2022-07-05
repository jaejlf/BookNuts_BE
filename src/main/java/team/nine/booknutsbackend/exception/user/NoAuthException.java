package team.nine.booknutsbackend.exception.user;

public class NoAuthException extends RuntimeException {
    public NoAuthException() {
        super("해당 유저는 수정/삭제 권한이 없습니다.");
    }
    public NoAuthException(String msg) {
        super(msg);
    }
}
