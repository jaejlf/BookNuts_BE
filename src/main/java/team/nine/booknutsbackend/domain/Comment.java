package team.nine.booknutsbackend.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private final String createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "writer")
    private User writer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parentId")
    private Comment parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private Set<Comment> children = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardId")
    private Board board;

    public Comment() {
    }

    public Comment(String content, User user, Board board, Comment parent) {
        this.content = content;
        this.writer = user;
        this.board = board;
        this.parent = parent;
    }

    public void clearContent() {
        this.content = null;
    }

}