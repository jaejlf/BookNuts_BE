package team.nine.booknutsbackend.domain.archive;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.request.ArchiveRequest;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Archive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long archiveId;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 100, nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    @Column(length = 300)
    private String imgUrl;

    @Column(nullable = false)
    private String createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    @OneToMany(mappedBy = "archive", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JsonIgnore
    private Set<ArchiveBoard> archiveBoardList = new HashSet<>();

    public Archive(ArchiveRequest archiveRequest, User user, String imgUrl) {
        this.title = archiveRequest.getTitle();
        this.content = archiveRequest.getContent();
        this.owner = user;
        this.imgUrl = imgUrl;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

}