package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.Comment;
import team.nine.booknutsbackend.dto.response.CommentResponse;
import team.nine.booknutsbackend.exception.board.BoardNotFoundException;
import team.nine.booknutsbackend.exception.comment.CommentNotFoundException;
import team.nine.booknutsbackend.repository.BoardRepository;
import team.nine.booknutsbackend.repository.CommentRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public Comment writeComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment writeReComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentList(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);
        List<Comment> comments = commentRepository.findByBoard(board);

        List<Comment> commentList = new ArrayList<>();
        List<CommentResponse> commentResponseList = new ArrayList<>();

        for (Comment comment : comments) {
            if (comment.getParent() == null) {
                commentList.add(comment);
                List<Comment> childComment = commentRepository.findByParent(comment);
                if (childComment != null) commentList.addAll(childComment);
            }
        }

        for (Comment comment : commentList) {
            commentResponseList.add(CommentResponse.commentResponse(comment));
        }

        return commentResponseList;

    }

}
