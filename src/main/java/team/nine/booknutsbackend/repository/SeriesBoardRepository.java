package team.nine.booknutsbackend.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.series.Series;
import team.nine.booknutsbackend.domain.series.SeriesBoard;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface SeriesBoardRepository extends JpaRepository<SeriesBoard, Long> {
    List<SeriesBoard> findBySeries(Series series);
    Optional<SeriesBoard> findByBoardAndSeries(Board board, Series series);

    @Transactional
    @Modifying
    @Query("delete from SeriesBoard s where s.series.seriesId = :seriesId")
    void deleteAllBySeriesId(@Param("seriesId") Long seriesId);
}