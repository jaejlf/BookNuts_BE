package team.nine.booknutsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    List<Comment> findByBoardAndParent(Board board, Comment parent);
    List<Comment> findByParent(Comment parent);
    Optional<Comment> findByBoardAndCommentId(Board board, Long commentId);
}