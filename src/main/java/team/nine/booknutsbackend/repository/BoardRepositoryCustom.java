package team.nine.booknutsbackend.repository;

import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.User;

import java.util.List;
import java.util.Optional;

public interface BoardRepositoryCustom {
    Optional<Board> findByBoardIdAndUser(Long boardId, User user);
    List<Board> findByUser(User user);
}
