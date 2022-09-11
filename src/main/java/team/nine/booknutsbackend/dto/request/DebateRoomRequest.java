package team.nine.booknutsbackend.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class DebateRoomRequest {
    @NotBlank String bookTitle;
    @NotBlank String bookAuthor;
    @NotBlank String bookImgUrl;
    @NotBlank String bookGenre;
    @NotBlank String topic;
    @NotNull int type;
    @NotNull int maxUser;
    @NotNull boolean opinion;
}