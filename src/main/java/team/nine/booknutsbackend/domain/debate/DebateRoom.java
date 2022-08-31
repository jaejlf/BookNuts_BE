package team.nine.booknutsbackend.domain.debate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.request.DebateRoomRequest;
import team.nine.booknutsbackend.enumerate.DebateStatus;
import team.nine.booknutsbackend.enumerate.DebateType;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static team.nine.booknutsbackend.enumerate.DebateStatus.READY;
import static team.nine.booknutsbackend.enumerate.DebateType.getDebateType;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
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

    @Enumerated(EnumType.STRING)
    private DebateType type;

    @Column(length = 100, nullable = false)
    private int maxUser; //총 토론 참여자 수 (2,4,6,8)

    @Column(length = 100, nullable = false)
    private int curYesUser; //현재 참여한 찬성 유저 수

    @Column(length = 100, nullable = false)
    private int curNoUser; //현재 참여한 반대 유저 수

    @Enumerated(EnumType.STRING)
    private DebateStatus status = READY;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "owner")
    private User owner;

    @JsonSerialize(using= LocalDateTimeSerializer.class)
    @JsonDeserialize(using= LocalDateTimeDeserializer.class)
    @Column(nullable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();

    public DebateRoom(DebateRoomRequest debateRoomRequest, User user, String coverImgUrl) {
        this.bookTitle = debateRoomRequest.getBookTitle();
        this.bookAuthor = debateRoomRequest.getBookAuthor();
        this.bookImgUrl = debateRoomRequest.getBookImgUrl();
        this.bookGenre = debateRoomRequest.getBookGenre();
        this.topic = debateRoomRequest.getTopic();
        this.type = getDebateType(debateRoomRequest.getType());
        this.maxUser = debateRoomRequest.getMaxUser();
        this.owner = user;
        this.coverImgUrl = coverImgUrl;
    }

    public void changeStatus(DebateStatus status) {
        this.status = status;
    }

    public void updateCurUser(int curYesUser, int curNoUser) {
        this.curYesUser = curYesUser;
        this.curNoUser = curNoUser;
    }

}