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

    public static CommentResponse commentResponse(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .createdDate(comment.getCreatedDate())
                .writer(comment.getUser().getNickname())
                .boardId(comment.getBoard().getBoardId())
                .build();
    }

//    public CommentResponse(Long commentId, String content, String createdDate, String writer){
//        this.commentId = commentId;
//        this.content = content;
//        this.createdDate = createdDate;
//        this.writer = writer;
//    }
//    public static CommentResponse converCommentToResponse(Comment comment){
//        return new CommentResponse(comment.getCommentId(), comment.getContent(), comment.getCreatedDate(), comment.getUser().getNickname());
//    }

}
