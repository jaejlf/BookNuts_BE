package team.nine.booknutsbackend.exception.s3;

public class EmptyFileException extends RuntimeException {
    public EmptyFileException() {
        super("파일이 존재하지 않습니다.");
    }
}
