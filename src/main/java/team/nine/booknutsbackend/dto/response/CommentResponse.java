package team.nine.booknutsbackend.dto.response;

import lombok.Builder;
import lombok.Getter;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.Comment;
import team.nine.booknutsbackend.domain.User;

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
                .parentId(comment.getParent().getCommentId())
                .boardId(comment.getBoard().getBoardId())
                .build();
    }

}
