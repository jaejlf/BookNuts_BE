package team.nine.booknutsbackend.dto.request;

import lombok.Getter;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.domain.debate.DebateRoom;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class DebateRoomRequest {

    @NotBlank String bookTitle;
    @NotBlank String bookAuthor;
    @NotBlank String bookImgUrl;
    @NotBlank String bookGenre;
    @NotBlank String topic;
    @NotNull int type;
    @NotNull int maxUser;
    @NotNull boolean opinion;
    User owner;

    public static DebateRoom roomRequest(DebateRoomRequest roomRequest, User user) {
        DebateRoom room = new DebateRoom();
        room.setBookTitle(roomRequest.getBookTitle());
        room.setBookAuthor(roomRequest.getBookAuthor());
        room.setBookImgUrl(roomRequest.getBookImgUrl());
        room.setBookGenre(roomRequest.getBookGenre());
        room.setTopic(roomRequest.getTopic());
        room.setType(roomRequest.getType());
        room.setMaxUser(roomRequest.getMaxUser());
        room.setOwner(user);

        return room;
    }

}
