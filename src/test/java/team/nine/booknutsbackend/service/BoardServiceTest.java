package team.nine.booknutsbackend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.dto.request.BoardRequest;
import team.nine.booknutsbackend.dto.response.BoardResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Board 서비스")
class BoardServiceTest extends CommonServiceTest {

    @Autowired
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

        //when
        BoardResponse result = boardService.writeBoard(boardRequest, user);

        //then
        assertAll(
                () -> assertThat(result.getTitle()).isEqualTo(boardRequest.getTitle()),
                () -> assertThat(result.getContent()).isEqualTo(boardRequest.getContent()),
                () -> assertThat(result.getBookTitle()).isEqualTo(boardRequest.getBookTitle()),
                () -> assertThat(result.getBookAuthor()).isEqualTo(boardRequest.getBookAuthor()),
                () -> assertThat(result.getBookImgUrl()).isEqualTo(boardRequest.getBookImgUrl()),
                () -> assertThat(result.getBookGenre()).isEqualTo(boardRequest.getBookGenre())
        );
    }

    @DisplayName("'나의 구독' 게시글 목록 조회")
    @Test
    void get0Boards() {
        //given & when
        List<Board> result = boardService.get0Boards(user);

        //then
        assertThat(result.size()).isEqualTo(0);
    }

    @DisplayName("'오늘 추천' 게시글 목록 조회")
    @Test
    void get1Boards() {
        //given & when
        List<Board> result = boardService.get1Boards();

        //then
        List<Board> expected = boardRepository.findAll();
        assertThat(result.size()).isEqualTo(expected.size());
    }

    @DisplayName("'독립 출판' 게시글 목록 조회")
    @Test
    void get2Boards() {
        //given & when
        List<Board> result = boardService.get2Boards();

        //then
        List<Board> expected = boardRepository.findBoardByGenre("독립서적");
        assertThat(result.size()).isEqualTo(expected.size());
    }

    @DisplayName("특정 유저의 게시글 목록 조회")
    @Test
    void getBoardListByUser() {
        //before
        boardRepository.save(new Board(
                "dum", "dum", "dum", "dum", "dum", "dum", user
        ));

        //given & when
        List<Board> result = boardService.getBoardListByUser(user);

        //then
        assertThat(result.size()).isEqualTo(1);
    }

}