package team.nine.booknutsbackend.exception.user;

public class ExpiredTokenException extends RuntimeException {
    public ExpiredTokenException() {
        super("토큰이 만료되었습니다.");
    }
}
