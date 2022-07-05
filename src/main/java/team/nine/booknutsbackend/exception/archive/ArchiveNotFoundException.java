package team.nine.booknutsbackend.exception.archive;

public class ArchiveNotFoundException extends RuntimeException {
    public ArchiveNotFoundException() {
        super("존재하지 않는 아카이브 아이디입니다.");
    }
}