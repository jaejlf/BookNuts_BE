package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
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

import static team.nine.booknutsbackend.exception.ErrorMessage.COMMENT_NOT_FOUND;
import static team.nine.booknutsbackend.exception.ErrorMessage.MOD_DEL_NO_AUTH;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardService boardService;

    @Transactional
    public CommentResponse writeParentComment(Map<String, String> commentRequest, Long boardId, User user) {
        Board board = boardService.getBoard(boardId);
        Comment comment = new Comment(commentRequest.get("content"), user, board, null);
        return CommentResponse.of(commentRepository.save(comment));
    }

    @Transactional
    public CommentResponse writeChildComment(Map<String, String> commentRequest, Long boardId, Long commentId, User user) {
        Board board = boardService.getBoard(boardId);
        Comment parent = getComment(commentId);
        Comment comment = new Comment(commentRequest.get("content"), user, board, parent);
        return CommentResponse.of(commentRepository.save(comment));
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentList(Long boardId) {
        Board board = boardService.getBoard(boardId);
        List<Comment> parentCommentList = commentRepository.findByBoardAndParent(board, null);

        List<Comment> commentList = new ArrayList<>();
        List<CommentResponse> commentResponseList = new ArrayList<>();

        for (Comment comment : parentCommentList) {
            commentList.add(comment);
            List<Comment> childCommentList = commentRepository.findByParent(comment);
            if (childCommentList != null) commentList.addAll(childCommentList);
        }

        for (Comment comment : commentList) {
            commentResponseList.add(CommentResponse.of(comment));
        }

        return commentResponseList;
    }

    @Transactional
    public void deleteComment(Long commentId, User user) {
        Comment comment = getComment(commentId);
        checkAuth(user, comment);

        if ((comment.getParent() != null) || (comment.getChildren().size() == 0)) {
            commentRepository.delete(comment);
        } else {
            comment.clearContent();
            commentRepository.save(comment);
        }
    }

    private void checkAuth(User user, Comment comment) {
        if (comment.getWriter() != user) throw new NoAuthException(MOD_DEL_NO_AUTH.getMsg());
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(COMMENT_NOT_FOUND.getMsg()));
    }

}
