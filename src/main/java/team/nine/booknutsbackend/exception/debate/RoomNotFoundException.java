package team.nine.booknutsbackend.exception.debate;

public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException() {
        super("존재하지 않는 토론장 아이디입니다.");
    }
}
