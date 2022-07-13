package team.nine.booknutsbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.Comment;
import team.nine.booknutsbackend.domain.User;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

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

//    private Long commentId;
//    private String content;
//    private Long userId;
//    private String nickname;
//    private List<CommentRequest> children = new ArrayList<>();
//
//    public CommentRequest(Long commentId,String content, Long userId, String nickname) {
//        this.commentId = commentId;
//        this.content = content;
//        this.userId = userId;
//        this.nickname = nickname;
//    }
//
//    public static CommentRequest convertCommentToRequest(Comment comment) {
//        return new CommentRequest(comment.getCommentId(), comment.getContent(), comment.getUser().getUserId(), comment.getUser().getNickname());
//    }
}
