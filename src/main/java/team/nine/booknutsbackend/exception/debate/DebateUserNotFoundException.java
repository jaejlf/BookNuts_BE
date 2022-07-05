package team.nine.booknutsbackend.exception.debate;

public class DebateUserNotFoundException extends RuntimeException {
    public DebateUserNotFoundException() {
        super("토론에 참여하고 있지 않은 유저입니다.");
    }
}
