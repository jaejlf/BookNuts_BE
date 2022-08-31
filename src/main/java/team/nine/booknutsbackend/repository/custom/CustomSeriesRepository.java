package team.nine.booknutsbackend.repository.custom;

import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.domain.series.Series;

import java.util.List;

public interface CustomSeriesRepository {
    List<Series> findAllByOwner(User user);
}
