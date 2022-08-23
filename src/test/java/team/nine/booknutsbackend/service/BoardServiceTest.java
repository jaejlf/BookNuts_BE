package team.nine.booknutsbackend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.dto.request.BoardRequest;
import team.nine.booknutsbackend.dto.response.BoardResponse;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("Board 서비스")
class BoardServiceTest extends CommonServiceTest {

    @InjectMocks
    private BoardService boardService;

    @DisplayName("게시글 작성")
    @Test
    void writeBoard() {
        //given
        BoardRequest boardRequest = new BoardRequest(
                "'땅콩은 콩일까'를 읽고",
                "땅콩이 콩인지 아닌지 궁금해졌다.",
                "땅콩은 콩일까?",
                "콩작가",
                "www.imgurl...",
                "어린이"
        );

        given(boardRepository.save(any())).willReturn(board);

        //when
        BoardResponse result = boardService.writeBoard(boardRequest, user);

        //then
        BoardResponse expected = BoardResponse.of(board, user);
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("게시글 목록 조회")
    @Test
    void getBoardListByType() {
        //given
        given(boardRepository.findAll()).willReturn(boardList());

        //when
        List<BoardResponse> result = boardService.getBoardListByType(user, 1);

        //then
        List<BoardResponse> expected = boardResponseList();
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("특정 유저의 게시글 목록 조회")
    @Test
    void getBoardListByUser() {
        //given
        given(boardRepository.findByWriter(any())).willReturn(boardList());

        //when
        List<BoardResponse> result = boardService.getBoardListByUser(user);

        //then
        List<BoardResponse> expected = boardResponseList();
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("게시글 조회")
    @Test
    void getBoardOne() {
        //given
        given(boardRepository.findById(any())).willReturn(Optional.ofNullable(board));

        //when
        BoardResponse result = boardService.getBoardOne(1L, user);

        //then
        BoardResponse expected = BoardResponse.of(board, user);
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("게시글 수정")
    @Test
    void updateBoard() {
        //given
        Map<String, String> modRequest = new HashMap<>();
        modRequest.put("title", "수정된 제목");
        modRequest.put("content", "수정된 내용");

        given(boardRepository.findById(any())).willReturn(Optional.ofNullable(board));
        given(boardRepository.save(any())).willReturn(updatedBoard());
        
        //when
        BoardResponse result = boardService.updateBoard(1L, modRequest, user);

        //then
        BoardResponse expected = BoardResponse.of(updatedBoard(), user);
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    /*
    Will Return Object
    */

    private List<Board> boardList() {
        List<Board> boardList = new ArrayList<>();
        boardList.add(board);
        return boardList;
    }

    private List<BoardResponse> boardResponseList() {
        List<BoardResponse> boardResponseList = new ArrayList<>();
        boardResponseList.add(BoardResponse.of(board, user));
        return boardResponseList;
    }
    
    private Board updatedBoard() {
        return new Board(
                "수정된 제목",
                "수정된 내용",
                "땅콩은 콩일까?",
                "콩작가",
                "www.imgurl...",
                "독립서적",
                user   
        );
    }

}