package team.nine.booknutsbackend.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private String createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commentWriter")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    private Comment parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardId")
    private Board board;

    public static Comment createComment(String content, Board board, User commentWriter, Comment parent) {
        Comment comment = new Comment();
        comment.content = content;
        comment.board = board;
        comment.user = commentWriter;
        comment.parent = parent;
        return comment;
    }

}
