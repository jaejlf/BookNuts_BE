package team.nine.booknutsbackend.exception.s3;

public class UploadFailedException extends RuntimeException {
    public UploadFailedException() {
        super("파일 업로드에 실패했습니다.");
    }
}
