package team.nine.booknutsbackend.dto.response;

import lombok.Builder;
import lombok.Getter;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.Comment;

@Getter
@Builder
public class CommentResponse {
    Long commentId;
    String content;
    String createdDate;
    String writer;
    Board board;
    Comment parent;

    public CommentResponse(Long commentId, String content, String createdDate, String writer){
        this.commentId = commentId;
        this.content = content;
        this.createdDate = createdDate;
        this.writer = writer;
    }
    public static CommentResponse converCommentToResponse(Comment comment){
        return new CommentResponse(comment.getCommentId(), comment.getContent(), comment.getCreatedDate(), comment.getUser().getNickname());
    }

}
