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
import team.nine.booknutsbackend.service.BoardService;
import team.nine.booknutsbackend.service.CommentService;
import team.nine.booknutsbackend.service.UserService;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final BoardService boardService;

    //댓글 작성(부모)
    @PostMapping("/{boardId}/write")
    public ResponseEntity<Object> writeComment(@PathVariable Long boardId, @RequestBody CommentRequest comment, Principal principal){
        User user = userService.findUserByEmail(principal.getName());
        Board board = boardService.getPost(boardId);
        Comment newComment = commentService.writeComment(CommentRequest.commentRequest(comment, user, board));
        return new ResponseEntity<>(CommentResponse.commentResponse(newComment), HttpStatus.CREATED);
    }

    //대댓글 작성
    @PostMapping("/{boardId}/{commentId}")
    public ResponseEntity<Object> writeReComment(@PathVariable("boardId") Long boardId, @PathVariable("commentId") Long commentId,
                                                 @RequestBody CommentRequest comment, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        Board board = boardService.getPost(boardId);
        Comment parentComment = commentService.getComment(commentId);
        Comment newComment = commentService.writeReComment(CommentRequest.RecommentRequest(comment, user, board, parentComment));
        return new ResponseEntity<>(CommentResponse.reCommentResponse(newComment), HttpStatus.CREATED);
    }

    //댓글 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<List<CommentResponse>> getComment(@PathVariable Long boardId, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        return new ResponseEntity<>(commentService.getCommentList(boardId), HttpStatus.OK);
    }

}
