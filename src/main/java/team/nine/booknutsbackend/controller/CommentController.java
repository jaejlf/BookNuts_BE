package team.nine.booknutsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.Comment;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.response.CommentResponse;
import team.nine.booknutsbackend.dto.response.ResultResponse;
import team.nine.booknutsbackend.service.BoardService;
import team.nine.booknutsbackend.service.CommentService;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final BoardService boardService;

    @PostMapping("/{boardId}/write")
    public ResponseEntity<Object> writeParentComment(@PathVariable Long boardId,
                                                     @RequestBody Map<String, String> commentRequest,
                                                     @AuthenticationPrincipal User user) {
        Board board = boardService.getBoard(boardId);
        CommentResponse newComment = commentService.writeParentComment(commentRequest, board, user);
        return ResponseEntity
                .status(CREATED)
                .body(ResultResponse.create("댓글 작성", newComment));
    }

    @PostMapping("/{boardId}/{commentId}")
    public ResponseEntity<Object> writeChildComment(@PathVariable("boardId") Long boardId,
                                                    @PathVariable("commentId") Long commentId,
                                                    @RequestBody Map<String, String> commentRequest,
                                                    @AuthenticationPrincipal User user) {
        Board board = boardService.getBoard(boardId);
        Comment parent = commentService.getComment(commentId);
        CommentResponse newComment = commentService.writeChildComment(commentRequest, board, parent, user);
        return ResponseEntity
                .status(CREATED)
                .body(ResultResponse.create(commentId + "번 댓글의 대댓글 작성", newComment));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<Object> getCommentList(@PathVariable Long boardId) {
        Board board = boardService.getBoard(boardId);
        List<CommentResponse> commentResponseList = commentService.getCommentList(board);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(boardId + "번 게시글의 댓글 목록 조회", commentResponseList));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Object> deleteComment(@PathVariable Long commentId,
                                                @AuthenticationPrincipal User user) {
        Comment comment = commentService.getComment(commentId);
        commentService.deleteComment(comment, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(commentId + "번 댓글 삭제"));
    }

}