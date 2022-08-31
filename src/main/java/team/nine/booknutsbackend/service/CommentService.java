package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.Comment;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.response.CommentResponse;
import team.nine.booknutsbackend.exception.user.NoAuthException;
import team.nine.booknutsbackend.repository.CommentRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static team.nine.booknutsbackend.exception.ErrorMessage.COMMENT_NOT_FOUND;
import static team.nine.booknutsbackend.exception.ErrorMessage.MOD_DEL_NO_AUTH;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponse writeParentComment(Map<String, String> commentRequest, Board board, User user) {
        Comment comment = new Comment(commentRequest.get("content"), user, board, null);
        return CommentResponse.of(commentRepository.save(comment));
    }

    @Transactional
    public CommentResponse writeChildComment(Map<String, String> commentRequest, Board board, Comment parent, User user) {
        checkReCommentEnable(parent.getCommentId(), board);
        Comment comment = new Comment(commentRequest.get("content"), user, board, parent);
        return CommentResponse.of(commentRepository.save(comment));
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentList(Board board) {
        List<Comment> parentCommentList = commentRepository.findByBoardAndParent(board, null);

        List<Comment> commentList = new ArrayList<>();
        for (Comment comment : parentCommentList) {
            commentList.add(comment);
            List<Comment> childCommentList = commentRepository.findByParent(comment);
            if (childCommentList != null) commentList.addAll(childCommentList);
        }

        return entityToDto(commentList);
    }

    @Transactional
    public void deleteComment(Comment comment, User user) {
        checkAuth(user, comment);

        if ((comment.getParent() != null) || (comment.getChildren().size() == 0)) {
            commentRepository.delete(comment);
        } else {
            comment.clearContent();
            commentRepository.save(comment);
        }
    }

    @Transactional(readOnly = true)
    @Cacheable(key = "#commentId", value = "getComment")
    public Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(COMMENT_NOT_FOUND.getMsg()));
    }

    private void checkAuth(User user, Comment comment) {
        if (!comment.getWriter().getNickname().equals(user.getNickname())) throw new NoAuthException(MOD_DEL_NO_AUTH.getMsg());
    }

    private void checkReCommentEnable(Long commentId, Board board) {
        commentRepository.findByBoardAndCommentId(board, commentId)
                .orElseThrow(() -> new EntityNotFoundException(COMMENT_NOT_FOUND.getMsg()));
    }

    public List<CommentResponse> entityToDto(List<Comment> commentList) {
        return commentList.stream().map(CommentResponse::of).collect(Collectors.toList());
    }

}
