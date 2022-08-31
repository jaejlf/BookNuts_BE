package team.nine.booknutsbackend.domain.series;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.request.SeriesRequest;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "series", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JsonIgnore
    private Set<SeriesBoard> seriesBoardList = new HashSet<>();

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

    public int getTotalNuts(Series series) {
        Set<SeriesBoard> seriesBoardList = series.getSeriesBoardList();
        int totalNuts = 0;
        for (SeriesBoard seriesBoard : seriesBoardList) {
            totalNuts += seriesBoard.getBoard().getNutsList().size();
        }
        return totalNuts;
    }

}