package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.domain.debate.DebateRoom;
import team.nine.booknutsbackend.dto.response.BoardResponse;
import team.nine.booknutsbackend.dto.response.DebateRoomResponse;
import team.nine.booknutsbackend.dto.response.UserProfileResponse;
import team.nine.booknutsbackend.repository.BoardRepository;
import team.nine.booknutsbackend.repository.DebateRoomRepository;
import team.nine.booknutsbackend.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static team.nine.booknutsbackend.service.SearchSpecs.*;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final BoardRepository boardRepository;
    private final DebateRoomRepository debateRoomRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<BoardResponse> searchBoard(String keyword, User user) {
        Specification<Board> spec = Specification
                .where(likeTitle(keyword))
                .or(likeContent(keyword))
                .or(likeBookTitle(keyword))
                .or(likeBookAuthor(keyword));

        List<Board> boards = boardRepository.findAll(spec);
        List<BoardResponse> boardDtoList = new ArrayList<>();

        for (Board board : boards) {
            boardDtoList.add(BoardResponse.boardResponse(board, user));
        }

        return boardDtoList;
    }

    @Transactional
    public List<DebateRoomResponse> searchRoom(String keyword) {
        Specification<DebateRoom> spec = Specification
                .where(likeTopic(keyword))
                .or(likeBookTitle(keyword))
                .or(likeBookAuthor(keyword));

        List<DebateRoom> rooms = debateRoomRepository.findAll(spec);
        List<DebateRoomResponse> roomDtoList = new ArrayList<>();

        for (DebateRoom room : rooms) {
            roomDtoList.add(DebateRoomResponse.roomResponse(room));
        }

        return roomDtoList;
    }

    @Transactional
    public List<UserProfileResponse> searchUser(String keyword, User curUser) {
        List<User> users = userRepository.findAllByNicknameContaining(keyword);
        List<UserProfileResponse> userProfileDto = new ArrayList<>();

        for (User targetUser : users) {
            userProfileDto.add(UserProfileResponse.userProfileResponse(curUser, targetUser));
        }

        return userProfileDto;
    }

}
