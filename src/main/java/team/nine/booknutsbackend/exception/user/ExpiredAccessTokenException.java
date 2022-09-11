package team.nine.booknutsbackend.exception.user;

public class ExpiredAccessTokenException extends RuntimeException {
    public ExpiredAccessTokenException() {
        super("만료된 액세스 토큰입니다.");
    }
}
