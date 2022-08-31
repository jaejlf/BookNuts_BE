package team.nine.booknutsbackend.repository.custom.impl;

import org.springframework.beans.factory.annotation.Autowired;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.domain.series.Series;
import team.nine.booknutsbackend.repository.custom.CustomSeriesRepository;

import javax.persistence.EntityManager;
import java.util.List;

public class CustomSeriesRepositoryImpl implements CustomSeriesRepository {

    @Autowired
    EntityManager em;

    @Override
    public List<Series> findAllByOwner(User owner) {
        return em.createQuery("select distinct s from Series s " +
                        "left join fetch s.owner left join fetch s.seriesBoardList " +
                        "where s.owner = :owner " +
                        "order by s.seriesId desc", Series.class)
                .setParameter("owner", owner)
                .getResultList();
    }

}
