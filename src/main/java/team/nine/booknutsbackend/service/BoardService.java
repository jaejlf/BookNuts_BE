package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.request.BoardRequest;
import team.nine.booknutsbackend.dto.response.BoardResponse;
import team.nine.booknutsbackend.exception.board.BoardNotFoundException;
import team.nine.booknutsbackend.exception.user.NoAuthException;
import team.nine.booknutsbackend.repository.BoardRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

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
    //*** 타입에 따른 반환 구현 전 ***
    @Transactional(readOnly = true)
    public List<BoardResponse> getBoard(User user, int type) {
        List<Board> boards = boardRepository.findAll();
        List<BoardResponse> boardDtoList = new ArrayList<>();

        for (Board board : boards) {
            boardDtoList.add(BoardResponse.boardResponse(board, user));
        }

        Collections.reverse(boardDtoList); //최신순
        return boardDtoList;
    }

    //내가 작성한 게시글 목록
    @Transactional(readOnly = true)
    public List<BoardResponse> getMyBoard(User user) {
        List<Board> myBoards = boardRepository.findByUser(user);
        List<BoardResponse> boardDtoList = new ArrayList<>();

        for (Board board : myBoards) {
            boardDtoList.add(BoardResponse.boardResponse(board, user));
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