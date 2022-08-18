package team.nine.booknutsbackend.exception.user;

public class NoAuthException extends RuntimeException {
    public NoAuthException(String msg) {
        super(msg);
    }
}
