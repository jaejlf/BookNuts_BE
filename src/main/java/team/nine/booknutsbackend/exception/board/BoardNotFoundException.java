package team.nine.booknutsbackend.exception.board;

public class BoardNotFoundException extends RuntimeException {
    public BoardNotFoundException() {
        super("존재하지 않는 게시글 아이디입니다.");
    }
}