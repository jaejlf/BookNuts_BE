package team.nine.booknutsbackend.exception.debate;

public class StatusChangeException extends RuntimeException {
    public StatusChangeException() {
        super("상태값은 토론 진행 중(=1) 또는 토론 종료(=2) 여야 합니다.");
    }
}
