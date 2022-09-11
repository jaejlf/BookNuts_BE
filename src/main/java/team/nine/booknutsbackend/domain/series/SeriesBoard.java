package team.nine.booknutsbackend.domain.series;

import lombok.Getter;
import lombok.NoArgsConstructor;
import team.nine.booknutsbackend.domain.Board;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
public class SeriesBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long SeriesBoardId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "series")
    private Series series;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board")
    private Board board;

    public SeriesBoard(Series series, Board board) {
        this.series = series;
        this.board = board;
    }

}