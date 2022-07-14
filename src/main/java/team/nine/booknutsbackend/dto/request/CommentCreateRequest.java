package team.nine.booknutsbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateRequest {
    private String content;
    private Long boardId;
    private Long userId;
    private Long parentId;
}
