package team.nine.booknutsbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.nine.booknutsbackend.domain.series.Series;
import team.nine.booknutsbackend.domain.series.SeriesBoard;

import java.util.Set;

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
                .totalNuts(getTotalNuts(series))
                .build();
    }

    private static int getTotalNuts(Series series) {
        Set<SeriesBoard> seriesBoardList = series.getSeriesBoardList();
        int totalNuts = 0;
        for (SeriesBoard seriesBoard : seriesBoardList) {
            totalNuts += seriesBoard.getBoard().getNutsList().size();
        }
        return totalNuts;
    }

}