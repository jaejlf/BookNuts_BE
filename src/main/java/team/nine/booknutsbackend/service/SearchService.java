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

        List<Board> boardList = boardRepository.findAll(spec);
        List<BoardResponse> boardResponseList = new ArrayList<>();
        for (Board board : boardList) {
            boardResponseList.add(BoardResponse.of(board, user));
        }
        return boardResponseList;
    }

    @Transactional
    public List<DebateRoomResponse> searchRoom(String keyword) {
        Specification<DebateRoom> spec = Specification
                .where(likeTopic(keyword))
                .or(likeBookTitle(keyword))
                .or(likeBookAuthor(keyword));
        List<DebateRoom> debateRoomList = debateRoomRepository.findAll(spec);
        List<DebateRoomResponse> debateRoomResponseList = new ArrayList<>();

        for (DebateRoom room : debateRoomList) {
            debateRoomResponseList.add(DebateRoomResponse.of(room));
        }
        return debateRoomResponseList;
    }

    @Transactional
    public List<UserProfileResponse> searchUser(String keyword, User me) {
        List<User> userList = userRepository.findAllByNicknameContaining(keyword);
        List<UserProfileResponse> userProfileResponseList = new ArrayList<>();
        for (User target : userList) {
            userProfileResponseList.add(UserProfileResponse.of(me, target));
        }
        return userProfileResponseList;
    }

}
