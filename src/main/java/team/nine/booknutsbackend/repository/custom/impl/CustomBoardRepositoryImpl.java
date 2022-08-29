package team.nine.booknutsbackend.repository.custom.impl;

import org.springframework.beans.factory.annotation.Autowired;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.repository.custom.CustomBoardRepository;

import javax.persistence.EntityManager;
import java.util.List;

public class CustomBoardRepositoryImpl implements CustomBoardRepository {

    @Autowired
    EntityManager em;

    @Override
    public List<Board> findBoardByWriter(User user) {
        return em.createQuery("select distinct b from Board b " +
                        "left join fetch b.writer " +
                        "left join fetch b.nutsList left join fetch b.heartList " +
                        "left join fetch b.archiveBoards left join fetch b.seriesBoards " +
                        "where b.writer = :user " +
                        "order by b.boardId desc", Board.class)
                        .setParameter("user", user)
                        .getResultList();
    }

    @Override
    public List<Board> findBoardByGenre(String genre) {
        return em.createQuery("select distinct b from Board b " +
                        "left join fetch b.writer " +
                        "left join fetch b.nutsList left join fetch b.heartList " +
                        "left join fetch b.archiveBoards left join fetch b.seriesBoards " +
                        "where b.bookGenre = :genre " +
                        "order by b.boardId desc", Board.class)
                        .setParameter("genre", genre)
                        .getResultList();
    }

    @Override
    public List<Board> findAllBoard() {
        return em.createQuery("select distinct b from Board b " +
                        "left join fetch b.writer " +
                        "left join fetch b.nutsList left join fetch b.heartList " +
                        "left join fetch b.archiveBoards left join fetch b.seriesBoards " +
                        "order by b.boardId desc", Board.class)
                        .getResultList();
    }

    @Override
    public Board findBoardById(Long boardId) {
        return em.createQuery("select distinct b from Board b " +
                        "left join fetch b.writer " +
                        "left join fetch b.nutsList left join fetch b.heartList " +
                        "left join fetch b.archiveBoards left join fetch b.seriesBoards " +
                        "where b.boardId = :boardId", Board.class)
                        .setParameter("boardId", boardId)
                        .getSingleResult();
    }

}
