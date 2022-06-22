package team.nine.booknutsbackend.exception.debate;

public class RoomNotFoundException extends IllegalArgumentException {
    public RoomNotFoundException(String msg) {
        super(msg);
    }
}
