package team.nine.booknutsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.request.BoardRequest;
import team.nine.booknutsbackend.dto.response.BoardResponse;
import team.nine.booknutsbackend.dto.response.ResultResponse;
import team.nine.booknutsbackend.service.BoardService;
import team.nine.booknutsbackend.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

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

    //탭별 게시글 목록 조회
    //나의 구독 = 0, 오늘 추천 = 1, 독립 출판 = 2
    @GetMapping("/list/type/{type}")
    public ResponseEntity<Object> getBoardListByType(@PathVariable int type,
                                                     @AuthenticationPrincipal User user) {
        List<BoardResponse> boardList = boardService.getBoardListByType(user, type);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok("type = " + type + " 게시글 목록 조회", boardList));
    }

    @GetMapping("/list/user/{userId}")
    public ResponseEntity<Object> getBoardListByUser(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        List<BoardResponse> boardList = boardService.getBoardListByUser(user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(userId + "번 유저가 작성한 게시글 목록 조회", boardList));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<Object> getBoardOne(@PathVariable Long boardId,
                                              @AuthenticationPrincipal User user) {
        BoardResponse board = boardService.getBoardOne(boardId, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(boardId + "번 게시글 조회", board));
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<Object> updateBoard(@PathVariable Long boardId,
                                              @RequestBody Map<String, String> modRequest,
                                              @AuthenticationPrincipal User user) {
        BoardResponse updatedBoard = boardService.updateBoard(boardId, modRequest, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.update(boardId + "번 게시글 수정", updatedBoard));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Object> deleteBoard(@PathVariable Long boardId,
                                              @AuthenticationPrincipal User user) {
        boardService.deleteBoard(boardId, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(boardId + "번 게시글 삭제"));
    }

}