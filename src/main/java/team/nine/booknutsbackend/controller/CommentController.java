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

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final BoardService boardService;

    @PostMapping("/{boardId}/write")
    public ResponseEntity<Object> writeComment(@PathVariable Long boardId, @RequestBody CommentRequest comment, Principal principal){
        User user = userService.findUserByEmail(principal.getName());
        Board board = boardService.getPost(boardId);
        Comment newComment = commentService.writeComment(CommentRequest.commentRequest(comment, user, board));
        return new ResponseEntity<>(CommentResponse.commentResponse(newComment), HttpStatus.CREATED);
    }

    @PostMapping("/{boardId}/{commentId}")
    public ResponseEntity<Object> writeReComment(@PathVariable("boardId") Long boardId, @PathVariable("commentId") Long commentId,
                                                 @RequestBody CommentRequest comment, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        Board board = boardService.getPost(boardId);
        Comment parentComment = commentService.getComment(commentId);
        Comment newComment = commentService.writeReComment(CommentRequest.RecommentRequest(comment, user, board, parentComment));
        return new ResponseEntity<>(CommentResponse.commentResponse(newComment), HttpStatus.CREATED);
    }

}
