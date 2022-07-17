package team.nine.booknutsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>{

    Optional<Comment> findById(Long commentId);
    List<Comment> findByBoard(Board board);

}
