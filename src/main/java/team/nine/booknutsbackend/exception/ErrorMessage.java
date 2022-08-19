package team.nine.booknutsbackend.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

    //Common
    DEBATE_NO_AUTH("토론 개설자만 상태를 변경할 수 있습니다."),
    MOD_DEL_NO_AUTH("해당 유저는 수정/삭제 권한이 없습니다."),

    //User
    USER_ALREADY_EXIST("이미 가입된 유저입니다."),
    USER_NOT_FOUND("존재하지 않는 유저입니다."),
    PASSWORD_ERROR("잘못된 비밀번호 입니다."),

    //Board
    BOARD_ALREADY_EXIST("이미 존재하는 게시글입니다."),
    BOARD_NOT_FOUND("존재하지 않는 게시글입니다."),
    TYPE_NUM_ERROR("type은 0 ~ 2 사이의 값이어야합니다."),

    //Archive
    ARCHIVE_NOT_FOUND("존재하지 않는 아카이브입니다."),

    //Series
    SERIES_NOT_FOUND("존재하지 않는 시리즈입니다."),

    //Comment
    COMMENT_NOT_FOUND("존재하지 않는 댓글입니다."),

    //Debate
    ROOM_NOT_FOUND("존재하지 않는 토론방입니다."),
    DEBATE_USER_ALREADY_EXIST("이미 토론에 참여 중인 유저입니다."),
    DEBATE_USER_NOT_FOUND("토론에 참여하고 있지 않은 유저입니다."),
    USER_EXCEED("인원 초과로 참여할 수 없습니다."),
    LOCKED_ROOM("토론이 진행 중이거나 종료되어 참여할 수 없습니다."),
    STATUS_NUM_ERROR("상태값은 토론 진행 중(=1) 또는 토론 종료(=2) 여야 합니다."),

    //Follow
    ALREADY_FOLLOWING("이미 팔로우 중인 계정입니다."),
    FOLLOW_NOT_FOUND("팔로우하고 있지 않은 계정입니다."),
    FOLLOW_ERROR("본인 계정은 팔로우할 수 없습니다."),

    //s3
    UPLOAD_FAILED("파일 업로드에 실패했습니다.");

    private final String msg;

}