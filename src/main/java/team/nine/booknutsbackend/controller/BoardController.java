package team.nine.booknutsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.request.BoardRequest;
import team.nine.booknutsbackend.dto.response.BoardResponse;
import team.nine.booknutsbackend.dto.response.ResultResponse;
import team.nine.booknutsbackend.enumerate.BoardType;
import team.nine.booknutsbackend.service.BoardService;
import team.nine.booknutsbackend.service.UserService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static team.nine.booknutsbackend.enumerate.BoardType.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;

    @PostMapping("/write")
    public ResponseEntity<Object> writeBoard(@RequestBody @Valid BoardRequest boardRequest,
                                             @AuthenticationPrincipal User user) {
        BoardResponse newBoard = boardService.writeBoard(boardRequest, user);
        return ResponseEntity
                .status(CREATED)
                .body(ResultResponse.create("게시글 작성", newBoard));
    }

    @GetMapping("/list/type/{type}")
    public ResponseEntity<Object> getBoardListByType(@PathVariable int type,
                                                     @AuthenticationPrincipal User user) {
        BoardType boardType = getBoardType(type);

        List<Board> boardList;
        if (boardType == MY) boardList = boardService.get0Boards(user);
        else if (boardType == TODAY) boardList = boardService.get1Boards();
        else boardList = boardService.get2Boards();

        List<BoardResponse> boardResponseList = boardService.entityToDto(boardList, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(BoardType.getBoardType(type) + " 게시글 목록 조회", boardResponseList));
    }

    @GetMapping("/list/user/{nickname}")
    public ResponseEntity<Object> getBoardListByUser(@PathVariable String nickname) {
        User user = userService.getUserByNickname(nickname);
        List<Board> boardList = boardService.getBoardListByUser(user);
        List<BoardResponse> boardResponseList = boardService.entityToDto(boardList, user);
        Collections.reverse(boardResponseList); //최신순
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok("유저 '" + nickname + "'(이)가 작성한 게시글 목록 조회", boardList));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<Object> getBoardOne(@PathVariable Long boardId,
                                              @AuthenticationPrincipal User user) {
        Board board = boardService.getBoard(boardId);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(boardId + "번 게시글 조회", BoardResponse.of(board, user)));
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<Object> updateBoard(@PathVariable Long boardId,
                                              @RequestBody Map<String, String> modRequest,
                                              @AuthenticationPrincipal User user) {
        Board board = boardService.getBoard(boardId);
        BoardResponse updatedBoard = boardService.updateBoard(board, modRequest, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.update(boardId + "번 게시글 수정", updatedBoard));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Object> deleteBoard(@PathVariable Long boardId,
                                              @AuthenticationPrincipal User user) {
        Board board = boardService.getBoard(boardId);
        boardService.deleteBoard(board, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(boardId + "번 게시글 삭제"));
    }

}