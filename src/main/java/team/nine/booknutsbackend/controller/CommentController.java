package team.nine.booknutsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.Comment;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.request.CommentRequest;
import team.nine.booknutsbackend.dto.response.CommentResponse;
import team.nine.booknutsbackend.exception.comment.NotNewCommentCreateException;
import team.nine.booknutsbackend.service.BoardService;
import team.nine.booknutsbackend.service.CommentService;
import team.nine.booknutsbackend.service.UserService;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final BoardService boardService;

    //댓글 작성(부모)
    @PostMapping("/{boardId}/write")
    public ResponseEntity<Object> writeComment(@PathVariable Long boardId, @RequestBody CommentRequest comment, Principal principal) {
        if (comment.getContent() == null) throw new NotNewCommentCreateException();
        User user = userService.findUserByEmail(principal.getName());
        Board board = boardService.getPost(boardId);
        Comment newComment = commentService.writeComment(CommentRequest.commentRequest(comment, user, board));
        return new ResponseEntity<>(CommentResponse.commentResponse(newComment), HttpStatus.CREATED);
    }

    //대댓글 작성
    @PostMapping("/{boardId}/{commentId}")
    public ResponseEntity<Object> writeReComment(@PathVariable("boardId") Long boardId, @PathVariable("commentId") Long commentId,
                                                 @RequestBody CommentRequest comment, Principal principal) {
        if (comment.getContent() == null) throw new NotNewCommentCreateException();
        User user = userService.findUserByEmail(principal.getName());
        Board board = boardService.getPost(boardId);
        Comment parentComment = commentService.getComment(commentId);
        Comment newComment = commentService.writeReComment(CommentRequest.RecommentRequest(comment, user, board, parentComment));
        return new ResponseEntity<>(CommentResponse.commentResponse(newComment), HttpStatus.CREATED);
    }

    //댓글 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<List<CommentResponse>> getComment(@PathVariable Long boardId, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        return new ResponseEntity<>(commentService.getCommentList(boardId), HttpStatus.OK);
    }

    //댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<Object> updateComment(@PathVariable Long commentId, @RequestBody CommentRequest comment, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        if (comment.getContent() == null) throw new NotNewCommentCreateException();
        Comment updateComment = commentService.updateComment(commentId, comment, user);
        return new ResponseEntity<>(CommentResponse.commentResponse(updateComment), HttpStatus.OK);
    }

    //댓글 삭제
    @DeleteMapping("{commentId}")
    public ResponseEntity<Object> deleteComment(@PathVariable Long commentId, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        commentService.deleteComment(commentId, user);

        Map<String, String> map = new HashMap<>();
        map.put("result", "삭제 완료");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
