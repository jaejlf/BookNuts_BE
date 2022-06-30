package team.nine.booknutsbackend.exception.user;

public class InvalidTokenException extends Exception {
    public InvalidTokenException(String msg) {
        super(msg);
    }
}