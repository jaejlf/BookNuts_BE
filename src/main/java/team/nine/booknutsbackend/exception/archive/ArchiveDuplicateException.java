package team.nine.booknutsbackend.exception.archive;

public class ArchiveDuplicateException extends RuntimeException {
    public ArchiveDuplicateException() {
        super("이미 아카이브에 존재하는 게시글 아이디입니다.");
    }
}