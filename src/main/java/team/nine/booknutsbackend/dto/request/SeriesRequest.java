package team.nine.booknutsbackend.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
public class SeriesRequest {
    @NotBlank String title;
    @NotBlank String content;
    List<Long> boardIdlist;
}