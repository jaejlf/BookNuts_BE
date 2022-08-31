package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.Follow;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.request.BoardRequest;
import team.nine.booknutsbackend.dto.response.BoardResponse;
import team.nine.booknutsbackend.exception.user.NoAuthException;
import team.nine.booknutsbackend.repository.BoardRepository;
import team.nine.booknutsbackend.repository.FollowRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static team.nine.booknutsbackend.exception.ErrorMessage.MOD_DEL_NO_AUTH;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final FollowRepository followRepository;

    @Transactional
    public BoardResponse writeBoard(BoardRequest boardRequest, User user) {
        Board board = new Board(
                boardRequest.getTitle(),
                boardRequest.getContent(),
                boardRequest.getBookTitle(),
                boardRequest.getBookAuthor(),
                boardRequest.getBookImgUrl(),
                boardRequest.getBookGenre(),
                user);
        return BoardResponse.of(boardRepository.save(board), user);
    }

    //나의 구독 = 0
    @Transactional(readOnly = true)
    @Cacheable(key = "#user.nickname", value = "get0Boards")
    public List<Board> get0Boards(User user) {
        List<Follow> followList = followRepository.findByFollower(user);
        List<Board> boardList = new ArrayList<>();
        for (Follow follow : followList) {
            List<Board> tmp = boardRepository.findBoardByWriter(follow.getFollower());
            if (!tmp.isEmpty()) boardList.addAll(tmp);
        }
        boardList.sort((a, b) -> (int) (a.getBoardId() - b.getBoardId())); //boardId 순 정렬
        return boardList;
    }

    //오늘 추천 = 1
    //임시로, 모든 게시글 리턴하도록 구현
    @Transactional(readOnly = true)
    @Cacheable(value = "get1Boards")
    public List<Board> get1Boards() {
        return boardRepository.findAllBoard();
    }

    //독립 출판 = 2
    @Transactional(readOnly = true)
    @Cacheable(value = "get2Boards")
    public List<Board> get2Boards() {
        return boardRepository.findBoardByGenre("독립서적");
    }

    @Transactional(readOnly = true)
    @Cacheable(key = "#boardId", value = "getBoard")
    public Board getBoard(Long boardId) {
        return boardRepository.findBoardById(boardId);
    }

    @Transactional(readOnly = true)
    @Cacheable(key = "#user.nickname", value = "getBoardListByUser")
    public List<Board> getBoardListByUser(User user) {
        return boardRepository.findBoardByWriter(user);
    }

    @Transactional
    public BoardResponse updateBoard(Board board, Map<String, String> modRequest, User user) {
        checkAuth(user, board);

        if (modRequest.get("title") != null) board.updateTitle(modRequest.get("title"));
        if (modRequest.get("content") != null) board.updateContent(modRequest.get("content"));

        return BoardResponse.of(boardRepository.save(board), user);
    }

    @Transactional
    public void deleteBoard(Board board, User user) {
        checkAuth(user, board);
        boardRepository.delete(board);
    }

    private void checkAuth(User user, Board board) {
        if (!board.getWriter().getNickname().equals(user.getNickname())) throw new NoAuthException(MOD_DEL_NO_AUTH.getMsg());
    }

    public List<BoardResponse> entityToDto(List<Board> boardList, User user) {
        return boardList.stream().map(x -> BoardResponse.of(x, user)).collect(Collectors.toList());
    }

}