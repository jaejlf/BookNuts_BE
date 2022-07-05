package team.nine.booknutsbackend.exception.series;

public class SeriesNotFoundException extends RuntimeException {
    public SeriesNotFoundException() {
        super("존재하지 않는 시리즈 아이디입니다.");
    }
}
