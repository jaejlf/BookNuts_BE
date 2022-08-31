package team.nine.booknutsbackend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.dto.request.BoardRequest;
import team.nine.booknutsbackend.dto.response.BoardResponse;

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
        BoardResponse expected = BoardResponse.of(new Board(
                boardRequest.getTitle(),
                boardRequest.getContent(),
                boardRequest.getBookTitle(),
                boardRequest.getBookAuthor(),
                boardRequest.getBookImgUrl(),
                boardRequest.getBookGenre(),
                user
        ), user);

        assertAll(
                () -> assertThat(result.getTitle()).isEqualTo(expected.getTitle()),
                () -> assertThat(result.getContent()).isEqualTo(expected.getContent()),
                () -> assertThat(result.getBookTitle()).isEqualTo(expected.getBookTitle()),
                () -> assertThat(result.getBookAuthor()).isEqualTo(expected.getBookAuthor()),
                () -> assertThat(result.getBookImgUrl()).isEqualTo(expected.getBookImgUrl()),
                () -> assertThat(result.getBookGenre()).isEqualTo(expected.getBookGenre()),
                () -> assertThat(result.getWriter()).isEqualTo(expected.getWriter())
        );
    }

    @DisplayName("게시글 목록 조회")
    @Test
    void getBoardListByType() {
//        //given
//        int type = 1;
//
//        //when
//        List<BoardResponse> result = boardService.getBoardListByType(user, type);
//
//        //then
//        List<Board> expected = boardRepository.findAll();
//        assertThat(result.size()).isEqualTo(expected.size());
    }

    @DisplayName("특정 유저의 게시글 목록 조회")
    @Test
    void getBoardListByUser() {
//        //given & when
//        List<BoardResponse> result = boardService.getBoardListByUser(user);
//
//        //then
//        List<Board> expected = boardRepository.findBoardByWriter(user);
//        assertThat(result.size()).isEqualTo(expected.size());
    }

    @DisplayName("게시글 조회")
    @Test
    void getBoardOne() {
//        //given
//        long boardId = 1L;
//
//        //when
//        BoardResponse result = boardService.getBoardOne(boardId, user);
//
//        //then
//        Optional<Board> expected = boardRepository.findById(1L);
//        assertAll(
//                () -> assertThat(result.getTitle()).isEqualTo(expected.get().getTitle()),
//                () -> assertThat(result.getContent()).isEqualTo(expected.get().getContent()),
//                () -> assertThat(result.getBookTitle()).isEqualTo(expected.get().getBookTitle()),
//                () -> assertThat(result.getBookAuthor()).isEqualTo(expected.get().getBookAuthor()),
//                () -> assertThat(result.getBookImgUrl()).isEqualTo(expected.get().getBookImgUrl()),
//                () -> assertThat(result.getBookGenre()).isEqualTo(expected.get().getBookGenre())
//        );
    }

}