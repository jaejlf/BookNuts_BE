package team.nine.booknutsbackend.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class ArchiveRequest {
    @NotBlank String title;
    @NotBlank String content;
}