package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.Follow;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.request.BoardRequest;
import team.nine.booknutsbackend.dto.response.BoardResponse;
import team.nine.booknutsbackend.enumerate.BoardType;
import team.nine.booknutsbackend.exception.user.NoAuthException;
import team.nine.booknutsbackend.repository.BoardRepository;
import team.nine.booknutsbackend.repository.FollowRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static team.nine.booknutsbackend.enumerate.BoardType.*;
import static team.nine.booknutsbackend.exception.ErrorMessage.*;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final FollowRepository followRepository;

    @Transactional
    public BoardResponse writeBoard(BoardRequest boardRequest, User user) {
        Board board = new Board(boardRequest, user);
        return BoardResponse.of(boardRepository.save(board), user);
    }

    @Transactional(readOnly = true)
    public List<BoardResponse> getBoardListByType(User user, int type) {
       List<Board> boardList = getBoardList(user, getBoardType(type));
        List<BoardResponse> boardResponseList = new ArrayList<>();
        for (Board board : boardList) {
            boardResponseList.add(BoardResponse.of(board, user));
        }
        Collections.reverse(boardResponseList); //최신순
        return boardResponseList;
    }

    @Transactional(readOnly = true)
    public List<BoardResponse> getBoardListByUser(User user) {
        List<Board> boardList = getBoardListByWriter(user);
        List<BoardResponse> boardResponseList = new ArrayList<>();
        for (Board board : boardList) {
            boardResponseList.add(BoardResponse.of(board, user));
        }
        Collections.reverse(boardResponseList); //최신순
        return boardResponseList;
    }

    @Transactional(readOnly = true)
    public BoardResponse getBoardOne(Long boardId, User user) {
        Board board = getBoard(boardId);
        return BoardResponse.of(board, user);
    }

    @Transactional(readOnly = true)
    public Board getBoard(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException(BOARD_NOT_FOUND.getMsg()));
    }

    @Transactional
    public BoardResponse updateBoard(Long boardId, Map<String, String> modRequest, User user) {
        Board board = getBoard(boardId);
        checkAuth(user, board);

        if (modRequest.get("title") != null) board.updateTitle(modRequest.get("title"));
        if (modRequest.get("content") != null) board.updateContent(modRequest.get("content"));

        return BoardResponse.of(boardRepository.save(board), user);
    }

    @Transactional
    public void deleteBoard(Long boardId, User user) {
        Board board = getBoard(boardId);
        checkAuth(user, board);
        boardRepository.delete(board);
    }

    private void checkAuth(User user, Board board) {
        if (board.getWriter() != user) throw new NoAuthException(MOD_DEL_NO_AUTH.getMsg());
    }

    //나의 구독 = 0
    private List<Board> get0Boards(User user) {
        List<Follow> followList = followRepository.findByFollower(user);
        List<Board> boardList = new ArrayList<>();
        for (Follow follow : followList) {
            List<Board> tmp = getBoardListByWriter(follow.getFollowing());
            if (!tmp.isEmpty()) boardList.addAll(tmp);
        }
        boardList.sort((a, b) -> (int) (a.getBoardId() - b.getBoardId())); //boardId 순 정렬
        return boardList;
    }

    //오늘 추천 = 1
    //임시로, 모든 게시글 리턴하도록 구현
    private List<Board> get1Boards() {
        return boardRepository.findAll();
    }

    //독립 출판 = 2
    private List<Board> get2Boards() {
        return boardRepository.findByBookGenre("독립서적");
    }

    private List<Board> getBoardListByWriter(User user) {
        return boardRepository.findByWriter(user);
    }

    private List<Board> getBoardList(User user, BoardType boardType) {
        List<Board> boardList;
        if (boardType == MY) boardList = get0Boards(user);
        else if (boardType == TODAY) boardList = get1Boards();
        else if (boardType == ONEPUB) boardList = get2Boards();
        else throw new IndexOutOfBoundsException(BOARD_TYPE_NUM_ERROR.getMsg());
        return boardList;
    }

}