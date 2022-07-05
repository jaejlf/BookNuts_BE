package team.nine.booknutsbackend.exception.follow;

public class AlreadyFollowingException extends RuntimeException {
    public AlreadyFollowingException() {
        super("이미 팔로잉 중인 계정입니다.");
    }
}