package team.nine.booknutsbackend.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoardType {

    MY(0), TODAY(1), ONEPUB(2); //나의 구독 = 0, 오늘 추천 = 1, 독립 출판 = 2

    private final int type;

    public static BoardType getBoardType(int type) {
        if (type == 0) return MY;
        else if (type == 1) return TODAY;
        else return ONEPUB;
    }

}

