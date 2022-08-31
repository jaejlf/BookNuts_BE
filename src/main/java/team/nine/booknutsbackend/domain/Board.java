package team.nine.booknutsbackend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.nine.booknutsbackend.domain.archive.ArchiveBoard;
import team.nine.booknutsbackend.domain.reaction.Heart;
import team.nine.booknutsbackend.domain.reaction.Nuts;
import team.nine.booknutsbackend.domain.series.SeriesBoard;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @JsonSerialize(using= LocalDateTimeSerializer.class)
    @JsonDeserialize(using= LocalDateTimeDeserializer.class)
    @Column(nullable = false)
    private final LocalDateTime createdDate = LocalDateTime.now();

    @Column(length = 100, nullable = false)
    private String bookTitle;

    @Column(length = 100, nullable = false)
    private String bookAuthor;

    @Column(length = 300, nullable = false)
    private String bookImgUrl;

    @Column(length = 100, nullable = false)
    private String bookGenre;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "writer")
    private User writer;

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    @JsonIgnore
    private Set<Nuts> nutsList = new HashSet<>();

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    @JsonIgnore
    private Set<Heart> heartList = new HashSet<>();

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    @JsonIgnore
    private Set<ArchiveBoard> archiveBoards = new HashSet<>();

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    @JsonIgnore
    private Set<SeriesBoard> seriesBoards = new HashSet<>();

    public Board(String title, String content, String bookTitle, String bookAuthor, String bookImgUrl, String bookGenre, User user) {
        this.title = title;
        this.content = content;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookImgUrl = bookImgUrl;
        this.bookGenre = bookGenre;
        this.writer = user;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

}