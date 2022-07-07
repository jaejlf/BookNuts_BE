package team.nine.booknutsbackend.exception.comment;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException() {
        super("존재하지 않는 댓글 아이디입니다.");
    }
}