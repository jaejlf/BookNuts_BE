package team.nine.booknutsbackend.repository.custom;

import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.User;

import java.util.List;

public interface CustomBoardRepository {
    List<Board> findBoardByWriter(User user);
    List<Board> findBoardByGenre(String genre);
    List<Board> findAllBoard();
    Board findBoardById(Long boardId);
    List<Board> findBoardLikeKeyword(String keyword);
}