package team.nine.booknutsbackend.dto.request;

import lombok.Getter;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.domain.series.Series;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
public class SeriesRequest {

    @NotBlank String title;
    @NotBlank String content;
    List<Long> boardIdlist;

    public static Series seriesRequest(SeriesRequest seriesRequest, User user) {
        Series series = new Series();
        series.setTitle(seriesRequest.getTitle());
        series.setContent(seriesRequest.getContent());
        series.setOwner(user);

        return series;
    }

}
