package team.nine.booknutsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.nine.booknutsbackend.domain.series.Series;
import team.nine.booknutsbackend.repository.custom.CustomSeriesRepository;

public interface SeriesRepository extends JpaRepository<Series, Long>, CustomSeriesRepository {
}