package team.nine.booknutsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.nine.booknutsbackend.domain.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository {

    @Query("select c from Comment c left join fetch c.parent where c.commentId = :commentId")
    List<Comment> findCommentByCommentIdWithParent(@Param("commentId") Long commentId);
}
