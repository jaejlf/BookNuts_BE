package team.nine.booknutsbackend.repository.custom.impl;

import org.springframework.beans.factory.annotation.Autowired;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.domain.archive.Archive;
import team.nine.booknutsbackend.repository.custom.CustomArchiveRepository;

import javax.persistence.EntityManager;
import java.util.List;

public class CustomArchiveRepositoryImpl implements CustomArchiveRepository {

    @Autowired
    EntityManager em;

    @Override
    public List<Archive> findAllByOwner(User owner) {
        return em.createQuery("select distinct a from Archive a " +
                        "left join fetch a.owner left join fetch a.archiveBoardList " +
                        "where a.owner = :owner " +
                        "order by a.archiveId desc", Archive.class)
                .setParameter("owner", owner)
                .getResultList();
    }

}
