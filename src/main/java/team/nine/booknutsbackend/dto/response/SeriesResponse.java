package team.nine.booknutsbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.nine.booknutsbackend.domain.series.Series;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeriesResponse {

    Long seriesId;
    String title;
    String content;
    String imgUrl;
    int totalPost;
    int totalNuts;

    public static SeriesResponse of(Series series) {
        return SeriesResponse.builder()
                .seriesId(series.getSeriesId())
                .title(series.getTitle())
                .content(series.getContent())
                .imgUrl(series.getImgUrl())
                .totalPost(series.getSeriesBoardList().size())
                .totalNuts(series.getTotalNuts(series))
                .build();
    }

}