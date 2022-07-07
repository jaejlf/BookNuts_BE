package team.nine.booknutsbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.nine.booknutsbackend.domain.Comment;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    private Long commentId;
    private String content;
    private Long userId;
    private String nickname;
    private List<CommentRequest> children = new ArrayList<>();

    public CommentRequest(Long commentId,String content, Long userId, String nickname) {
        this.commentId = commentId;
        this.content = content;
        this.userId = userId;
        this.nickname = nickname;
    }

    public static CommentRequest convertCommentToRequest(Comment comment) {
        return new CommentRequest(comment.getCommentId(), comment.getContent(), comment.getUser().getUserId(), comment.getUser().getNickname());
    }
}
