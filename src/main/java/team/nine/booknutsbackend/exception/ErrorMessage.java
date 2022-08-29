package team.nine.booknutsbackend.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

    MOD_DEL_NO_AUTH("해당 유저는 수정/삭제/변경 권한이 없습니다."),
    USER_NOT_FOUND("존재하지 않는 유저입니다."),
    PASSWORD_ERROR("잘못된 비밀번호 입니다."),

    BOARD_ALREADY_EXIST("이미 존재하는 게시글입니다."),
    BOARD_TYPE_NUM_ERROR("type은 0 ~ 2 사이의 값이어야합니다."),

    BOARD_NOT_FOUND("존재하지 않는 게시글입니다."),
    ARCHIVE_NOT_FOUND("존재하지 않는 아카이브입니다."),
    SERIES_NOT_FOUND("존재하지 않는 시리즈입니다."),
    COMMENT_NOT_FOUND("존재하지 않는 댓글입니다."),
    ROOM_NOT_FOUND("존재하지 않는 토론방입니다."),

    DEBATE_USER_ALREADY_EXIST("이미 토론에 참여 중인 유저입니다."),
    DEBATE_USER_NOT_FOUND("토론에 참여하고 있지 않은 유저입니다."),
    DEBATE_USER_EXCEED("인원 초과로 참여할 수 없습니다."),
    LOCKED_ROOM("토론이 진행 중이거나 종료되어 참여할 수 없습니다."),
    STATUS_NUM_ERROR("상태값은 토론 진행 중(=1) 또는 토론 종료(=2) 여야 합니다."),
    DEBATE_TYPE_NUM_ERROR("타입 값은 텍스트 채팅(=0) 또는 음성 채팅(=1) 이어야 합니다.");

    private final String msg;

}