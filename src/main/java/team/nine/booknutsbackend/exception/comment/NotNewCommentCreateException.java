package team.nine.booknutsbackend.exception.comment;

public class NotNewCommentCreateException extends RuntimeException {
    public NotNewCommentCreateException() { super("댓글 내용을 작성하셔야 합니다"); }
}
