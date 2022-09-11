package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(key = "#keyword", value = "searchBoard")
    public List<Board> searchBoard(String keyword) {
        return boardRepository.findBoardLikeKeyword(keyword);
    }

    @Transactional
    @Cacheable(key = "#keyword", value = "searchRoom")
    public List<DebateRoom> searchRoom(String keyword) {
        Specification<DebateRoom> spec = Specification
                .where(likeTopic(keyword))
                .or(likeBookTitle(keyword))
                .or(likeBookAuthor(keyword));
        return debateRoomRepository.findAll(spec);
    }

    @Transactional
    @Cacheable(key = "#keyword", value = "searchUser")
    public List<User> searchUser(String keyword) {
        return userRepository.findAllByNicknameContaining(keyword);
    }

    public List<BoardResponse> entityToDto(List<Board> boardList, User user) {
        return boardList.stream().map(x -> BoardResponse.of(x, user)).collect(Collectors.toList());
    }

    public List<DebateRoomResponse> entityToDto(List<DebateRoom> debateRoomList) {
        return debateRoomList.stream().map(DebateRoomResponse::of).collect(Collectors.toList());
    }

    public List<UserProfileResponse> entityToDto(User me, List<User> userList) {
        return userList.stream().map(x -> UserProfileResponse.of(me, x)).collect(Collectors.toList());
    }

}
