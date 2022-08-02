package team.nine.booknutsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    List<Comment> findByBoard(Board board);
    List<Comment> findByParent(Comment comment);
}
