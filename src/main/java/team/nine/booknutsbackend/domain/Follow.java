package team.nine.booknutsbackend.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "following")
    private User following;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "follower")
    private User follower;

    public Follow(User following, User follower) {
        this.following = following;
        this.follower = follower;
    }

}