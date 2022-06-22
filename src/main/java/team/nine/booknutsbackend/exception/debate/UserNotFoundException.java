package team.nine.booknutsbackend.exception.debate;

public class UserNotFoundException extends IllegalArgumentException {
    public UserNotFoundException(String msg) {
        super(msg);
    }
}
