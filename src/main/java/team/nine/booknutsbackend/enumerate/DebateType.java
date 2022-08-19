package team.nine.booknutsbackend.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static team.nine.booknutsbackend.exception.ErrorMessage.DEBATE_TYPE_NUM_ERROR;

@Getter
@AllArgsConstructor
public enum DebateType {

    TEXT(0), CALL(1), ALL(2);

    private final int type;

    public static DebateType getDebateType(int type) {
        if (type == 0) return TEXT;
        else if (type == 1) return CALL;
        else if (type == 2) return ALL;
        else throw new IllegalArgumentException(DEBATE_TYPE_NUM_ERROR.getMsg());
    }

}