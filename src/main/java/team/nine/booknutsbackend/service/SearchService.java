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
import java.util.List;
import java.util.stream.Collectors;

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
        return entityToDto(boardList, user);
    }

    @Transactional
    public List<DebateRoomResponse> searchRoom(String keyword) {
        Specification<DebateRoom> spec = Specification
                .where(likeTopic(keyword))
                .or(likeBookTitle(keyword))
                .or(likeBookAuthor(keyword));
        List<DebateRoom> debateRoomList = debateRoomRepository.findAll(spec);
        return entityToDto(debateRoomList);
    }

    @Transactional
    public List<UserProfileResponse> searchUser(String keyword, User me) {
        List<User> userList = userRepository.findAllByNicknameContaining(keyword);
        return entityToDto(me, userList);
    }

    private List<BoardResponse> entityToDto(List<Board> boardList, User user) {
        return boardList.stream().map(x -> BoardResponse.of(x, user)).collect(Collectors.toList());
    }

    private List<DebateRoomResponse> entityToDto(List<DebateRoom> debateRoomList) {
        return debateRoomList.stream().map(DebateRoomResponse::of).collect(Collectors.toList());
    }

    private List<UserProfileResponse> entityToDto(User me, List<User> userList) {
        return userList.stream().map(x -> UserProfileResponse.of(me, x)).collect(Collectors.toList());
    }

}
