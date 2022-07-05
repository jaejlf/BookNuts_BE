package team.nine.booknutsbackend.exception.follow;

public class NotFollowingException extends RuntimeException {
    public NotFollowingException() {
        super("팔로우하고 있지 않은 계정입니다.");
    }
}
