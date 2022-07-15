package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.Follow;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.request.BoardRequest;
import team.nine.booknutsbackend.dto.response.BoardResponse;
import team.nine.booknutsbackend.exception.board.BoardNotFoundException;
import team.nine.booknutsbackend.exception.board.OutOfIndexException;
import team.nine.booknutsbackend.exception.user.NoAuthException;
import team.nine.booknutsbackend.repository.BoardRepository;
import team.nine.booknutsbackend.repository.FollowRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final FollowRepository followRepository;

    //특정 게시글 조회
    @Transactional(readOnly = true)
    public Board getPost(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);
    }

    //게시글 작성
    @Transactional
    public Board writePost(Board newBoard) {
        return boardRepository.save(newBoard);
    }

    //게시글 목록 조회
    //나의 구독 = 0, 오늘 추천 = 1, 독립 출판 = 2
    @Transactional(readOnly = true)
    public List<BoardResponse> getBoard(User user, int type) {
        if (type < 0 || type > 2) throw new OutOfIndexException();

        List<Board> boards;
        if (type == 0) boards = get0Boards(user);
        else if (type == 1) boards = get1Boards();
        else boards = get2Boards();

        List<BoardResponse> boardDtoList = new ArrayList<>();
        for (Board board : boards) {
            boardDtoList.add(BoardResponse.boardResponse(board, user));
        }

        Collections.reverse(boardDtoList); //최신순
        return boardDtoList;
    }

    //나의 구독 = 0
    @Transactional(readOnly = true)
    public List<Board> get0Boards(User user) {
        List<Follow> followList = followRepository.findByFollower(user);
        List<Board> boards = new ArrayList<>();
        for (Follow follow : followList) {
            List<Board> tmp = boardRepository.findByUser(follow.getFollowing());
            if (!tmp.isEmpty()) boards.addAll(tmp);
        }

        boards.sort((a, b) -> (int) (a.getBoardId() - b.getBoardId())); //boardId 순 정렬
        return boards;
    }

    //오늘 추천 = 1
    //임시로, 모든 게시글 리턴하도록 구현
    @Transactional(readOnly = true)
    public List<Board> get1Boards() {
        return boardRepository.findAll();
    }

    //독립 출판 = 2
    @Transactional(readOnly = true)
    public List<Board> get2Boards() {
        return boardRepository.findByBookGenre("독립서적");
    }

    //특정 유저의 게시글 목록 조회
    @Transactional(readOnly = true)
    public List<BoardResponse> getBoardList(User owner) {
        List<Board> boards = boardRepository.findByUser(owner);
        List<BoardResponse> boardDtoList = new ArrayList<>();

        for (Board board : boards) {
            boardDtoList.add(BoardResponse.boardResponse(board, owner));
        }

        Collections.reverse(boardDtoList); //최신순
        return boardDtoList;
    }

    //게시글 수정
    @Transactional
    public Board updatePost(Long boardId, BoardRequest boardRequest, User user) {
        Board board = getPost(boardId);
        if (board.getUser() != user) throw new NoAuthException();

        if (boardRequest.getTitle() != null) board.setTitle(boardRequest.getTitle());
        if (boardRequest.getContent() != null) board.setContent(boardRequest.getContent());

        return boardRepository.save(board);
    }

    //게시글 삭제
    @Transactional
    public void deletePost(Long boardId, User user) {
        Board board = getPost(boardId);
        if (board.getUser() != user) throw new NoAuthException();
        boardRepository.delete(board);
    }

}