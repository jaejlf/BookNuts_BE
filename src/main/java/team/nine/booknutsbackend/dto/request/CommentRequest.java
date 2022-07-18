package team.nine.booknutsbackend.dto.request;

import lombok.Getter;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.Comment;
import team.nine.booknutsbackend.domain.User;

import javax.validation.constraints.NotBlank;

@Getter
public class CommentRequest {

    @NotBlank String content;

    public static Comment commentRequest(CommentRequest commentRequest, User user, Board board) {
        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setUser(user);
        comment.setBoard(board);

        return comment;
    }

    public static Comment RecommentRequest(CommentRequest commentRequest, User user, Board board, Comment parentCommnet) {
        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setParent(parentCommnet);
        comment.setUser(user);
        comment.setBoard(board);

        return comment;
    }

}
