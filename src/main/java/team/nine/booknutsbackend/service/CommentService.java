package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.Comment;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.request.CommentCreateRequest;
import team.nine.booknutsbackend.dto.request.CommentRequest;
import team.nine.booknutsbackend.dto.response.CommentResponse;
import team.nine.booknutsbackend.exception.board.BoardNotFoundException;
import team.nine.booknutsbackend.exception.comment.CommentNotFoundException;
import team.nine.booknutsbackend.repository.BoardRepository;
import team.nine.booknutsbackend.repository.CommentRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentService {

    private BoardRepository boardRepository;
    private CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public List<CommentRequest> findCommentsByBoardId(Long boardId) {
        //존재하는 게시글인지 확인
        return convertNestedStructure(commentRepository.findCommentByCommentIdWithParent(boardId));
    }

    @Transactional
    public CommentRequest createComment(CommentCreateRequest commentCreateRequest, User user) {
        Board board = boardRepository.findById(commentCreateRequest.getBoardId())
                .orElseThrow(BoardNotFoundException::new);
        Comment comment = commentRepository.save(
                Comment.createComment(commentCreateRequest.getContent(), board, user,
                        commentCreateRequest.getParentId() != null ?
                                commentRepository.findById(commentCreateRequest.getParentId()).orElseThrow(CommentNotFoundException::new) : null)
                        );
        return CommentRequest.convertCommentToRequest(comment);
    }

    private List<CommentRequest> convertNestedStructure(List<Comment> comments) {
        List<CommentRequest> commentsResult = new ArrayList<>();
        Map<Long, CommentRequest> commentRequestMap = new HashMap<>();
        comments.stream().forEach(c -> {
            CommentRequest commentRequest = CommentRequest.convertCommentToRequest(c);
            commentRequestMap.put(commentRequest.getCommentId(), commentRequest);
            if(c.getParent() != null) commentRequestMap.get(c.getParent().getCommentId()).getChildren().add(commentRequest);
            else commentsResult.add(commentRequest);
        });
        return  commentsResult;
    }
}
