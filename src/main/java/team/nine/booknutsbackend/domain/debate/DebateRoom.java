package team.nine.booknutsbackend.domain.debate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.request.DebateRoomRequest;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
public class DebateRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long debateRoomId;

    @Column(length = 100, nullable = false)
    private String bookTitle;

    @Column(length = 100, nullable = false)
    private String bookAuthor;

    @Column(length = 300)
    private String bookImgUrl;

    @Column(length = 100, nullable = false)
    private String bookGenre;

    @Column(length = 100, nullable = false)
    private String topic;

    @Column(length = 300, nullable = false)
    private String coverImgUrl;

    @Column(length = 100, nullable = false)
    private int type; //0 : 채팅, 1 : 음성

    @Column(length = 100, nullable = false)
    private int maxUser; //총 토론 참여자 수 (2,4,6,8)

    @Column(length = 100, nullable = false)
    private int curYesUser; //현재 참여한 찬성 유저 수

    @Column(length = 100, nullable = false)
    private int curNoUser; //현재 참여한 반대 유저 수

    @Column(length = 100, nullable = false)
    private int status = 0; //0 : 토론 대기 중, 1 : 토론 진행 중, 2 : 토론 종료

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "owner")
    private User owner;

    @Column(nullable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();

    public DebateRoom(DebateRoomRequest debateRoomRequest, User user, String coverImgUrl) {
        this.bookTitle = debateRoomRequest.getBookTitle();
        this.bookAuthor = debateRoomRequest.getBookAuthor();
        this.bookImgUrl = debateRoomRequest.getBookImgUrl();
        this.bookGenre = debateRoomRequest.getBookGenre();
        this.topic = debateRoomRequest.getTopic();
        this.type = debateRoomRequest.getType();
        this.maxUser = debateRoomRequest.getMaxUser();
        this.owner = user;
        this.coverImgUrl = coverImgUrl;
    }

    public void changeStatus(int status) {
        this.status = status;
    }

    public void updateCurUser(int curYesUser, int curNoUser) {
        this.curYesUser = curYesUser;
        this.curNoUser = curNoUser;
    }

}