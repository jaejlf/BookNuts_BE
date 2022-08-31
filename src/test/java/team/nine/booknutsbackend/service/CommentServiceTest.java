package team.nine.booknutsbackend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.Comment;
import team.nine.booknutsbackend.dto.response.CommentResponse;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Comment 서비스")
class CommentServiceTest extends CommonServiceTest {

    @Autowired
    private CommentService commentService;

    @DisplayName("댓글 작성")
    @Test
    void writeParentComment() {
        //before
        Board board = boardRepository.save(new Board("dum", "dum", "dum", "dum", "dum", "dum", user));

        //given
        Map<String, String> commentRequest = new HashMap<>();
        commentRequest.put("content", "댓글!");

        //when
        CommentResponse result = commentService.writeParentComment(commentRequest, board, user);

        //then
        assertAll(
                () -> assertThat(result.getContent()).isEqualTo(commentRequest.get("content")),
                () -> assertThat(result.getBoardId()).isEqualTo(board.getBoardId())
        );
    }

    @DisplayName("대댓글 작성")
    @Test
    void writeChildComment() {
        //before
        Board board = boardRepository.save(new Board("dum", "dum", "dum", "dum", "dum", "dum", user));
        Comment comment = commentRepository.save(new Comment("내용", user, board, null));

        //given
        Map<String, String> commentRequest = new HashMap<>();
        commentRequest.put("content", "대댓글!");

        //when
        CommentResponse result = commentService.writeChildComment(commentRequest, board, comment, user);

        //then
        assertAll(
                () -> assertThat(result.getContent()).isEqualTo(commentRequest.get("content")),
                () -> assertThat(result.getBoardId()).isEqualTo(board.getBoardId()),
                () -> assertThat(result.getParentId()).isEqualTo(comment.getCommentId())
        );
    }

}