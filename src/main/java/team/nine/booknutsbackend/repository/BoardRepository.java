package team.nine.booknutsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.repository.custom.CustomBoardRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, CustomBoardRepository {
}