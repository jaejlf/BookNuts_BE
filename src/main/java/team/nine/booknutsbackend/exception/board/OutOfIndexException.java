package team.nine.booknutsbackend.exception.board;

public class OutOfIndexException extends RuntimeException {
    public OutOfIndexException() {
        super("type은 0 ~ 2 사이의 값이어야합니다.");
    }
}
