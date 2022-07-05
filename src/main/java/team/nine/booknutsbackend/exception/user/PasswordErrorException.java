package team.nine.booknutsbackend.exception.user;

public class PasswordErrorException extends RuntimeException {
    public PasswordErrorException() {
        super("잘못된 비밀번호 입니다.");
    }
}
