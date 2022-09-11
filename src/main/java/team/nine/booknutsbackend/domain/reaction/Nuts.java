package team.nine.booknutsbackend.domain.reaction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.User;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
public class Nuts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nutsId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board")
    private Board board;

    public Nuts(Board board, User user) {
        this.board = board;
        this.user = user;
    }

}