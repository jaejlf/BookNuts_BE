package team.nine.booknutsbackend.domain.series;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.request.SeriesRequest;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
public class Series {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seriesId;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(length = 300)
    private String imgUrl;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "owner")
    private User owner;

    @OneToMany(mappedBy = "series", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<SeriesBoard> seriesBoardList = new ArrayList<>();

    public Series(SeriesRequest seriesRequest, User user, String imgUrl) {
        this.title = seriesRequest.getTitle();
        this.content = seriesRequest.getContent();
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