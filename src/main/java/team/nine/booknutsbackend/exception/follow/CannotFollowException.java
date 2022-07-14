package team.nine.booknutsbackend.exception.follow;

public class CannotFollowException extends RuntimeException{
    public CannotFollowException() {
        super("팔로우할 수 없는 계정입니다.");
    }
}
