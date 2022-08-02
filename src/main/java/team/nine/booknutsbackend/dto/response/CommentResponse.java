package team.nine.booknutsbackend.dto.response;

import lombok.Builder;
import lombok.Getter;
import team.nine.booknutsbackend.domain.Comment;

@Getter
@Builder
public class CommentResponse {

    Long commentId;
    String content;
    String createdDate;
    String writer;
    Long boardId;
    Long parentId;

    public static CommentResponse commentResponse(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .createdDate(comment.getCreatedDate())
                .writer(comment.getUser().getNickname())
                .parentId(getParent(comment))
                .boardId(comment.getBoard().getBoardId())
                .build();
    }

    private static Long getParent(Comment comment) {
        if (comment.getParent() == null) return null;
        else return comment.getParent().getCommentId();
    }

}
