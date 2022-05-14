package team.nine.booknutsbackend.domain.myStory;

import lombok.Getter;
import lombok.Setter;
import team.nine.booknutsbackend.domain.User;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class MyStory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long myStoryId;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(length = 100, nullable = false)
    private String imgUrl;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name ="user")
    private User user;
}
