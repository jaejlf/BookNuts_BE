package team.nine.booknutsbackend.exception.series;

public class SeriesDuplicateException extends RuntimeException {
    public SeriesDuplicateException() {
        super("이미 시리즈에 존재하는 게시글 아이디입니다.");
    }
}