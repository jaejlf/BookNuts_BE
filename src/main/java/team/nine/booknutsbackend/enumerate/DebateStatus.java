package team.nine.booknutsbackend.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static team.nine.booknutsbackend.exception.ErrorMessage.STATUS_NUM_ERROR;

@Getter
@AllArgsConstructor
public enum DebateStatus {

    READY("대기 중", 0),
    ING("진행 중", 1),
    END("종료", 2);

    private final String message;
    private final int statusCode;

    public static String getMsgByCode(int statusCode) {
        if (statusCode == 0) return READY.getMessage();
        else if (statusCode == 1) return ING.getMessage();
        else if (statusCode == 2) return END.getMessage();
        else throw new IllegalArgumentException(STATUS_NUM_ERROR.getMsg());
    }

    public static DebateStatus getStatusByCode(int statusCode) {
        if (statusCode == 0) return READY;
        else if (statusCode == 1) return ING;
        else if (statusCode == 2) return END;
        else throw new IllegalArgumentException(STATUS_NUM_ERROR.getMsg());
    }

}