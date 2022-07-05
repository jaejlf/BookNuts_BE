package team.nine.booknutsbackend.exception.user;

public class ExpiredRefreshTokenException extends RuntimeException {
    public ExpiredRefreshTokenException() {
        super("오랜 시간 사용하지 않아, refresh 토큰이 만료되었습니다.");
    }
}
