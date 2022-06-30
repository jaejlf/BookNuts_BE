package team.nine.booknutsbackend.exception.s3;

public class EmptyFileException extends Exception {
    public EmptyFileException(String msg) {
        super(msg);
    }
}
