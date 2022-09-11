package team.nine.booknutsbackend.domain.debate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import team.nine.booknutsbackend.domain.User;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
public class DebateUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long debateUserId;

    @Column(nullable = false)
    private Boolean opinion;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "debateRoom")
    private DebateRoom debateRoom;

    public DebateUser(User user, DebateRoom debateRoom, boolean opinion) {
        this.user = user;
        this.debateRoom = debateRoom;
        this.opinion = opinion;
    }

}