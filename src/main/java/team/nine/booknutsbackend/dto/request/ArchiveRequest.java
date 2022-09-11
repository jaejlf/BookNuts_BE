package team.nine.booknutsbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveRequest {
    @NotBlank String title;
    @NotBlank String content;
}