package team.nine.booknutsbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.nine.booknutsbackend.domain.Comment;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    Long commentId;
    String content;
    LocalDateTime createdDate;
    String writer;
    Long boardId;
    Long parentId;

    public static CommentResponse of(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .createdDate(comment.getCreatedDate())
                .writer(comment.getWriter().getNickname())
                .parentId(getParent(comment))
                .boardId(comment.getBoard().getBoardId())
                .build();
    }

    private static Long getParent(Comment comment) {
        if (comment.getParent() == null) return null;
        else return comment.getParent().getCommentId();
    }

}