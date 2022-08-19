package team.nine.booknutsbackend.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class BoardRequest {
    @NotBlank String title;
    @NotBlank String content;
    @NotBlank String bookTitle;
    @NotBlank String bookAuthor;
    @NotBlank String bookImgUrl;
    @NotBlank String bookGenre;
}