package team.nine.booknutsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.domain.series.Series;
import team.nine.booknutsbackend.domain.series.SeriesBoard;
import team.nine.booknutsbackend.dto.request.SeriesRequest;
import team.nine.booknutsbackend.dto.response.BoardResponse;
import team.nine.booknutsbackend.dto.response.ResultResponse;
import team.nine.booknutsbackend.dto.response.SeriesResponse;
import team.nine.booknutsbackend.service.BoardService;
import team.nine.booknutsbackend.service.SeriesService;
import team.nine.booknutsbackend.service.UserService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/series")
public class SeriesController {

    private final SeriesService seriesService;
    private final UserService userService;
    private final BoardService boardService;

    @GetMapping("/list/{nickname}")
    public ResponseEntity<Object> getSeriesList(@PathVariable String nickname) {
        User user = userService.getUserByNickname(nickname);
        List<SeriesResponse> seriesList = seriesService.getSeriesList(user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok("유저 '" + nickname + "'의 시리즈 목록 조회", seriesList));
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createSeries(@RequestPart(value = "file", required = false) MultipartFile file,
                                               @RequestPart(value = "series") @Valid SeriesRequest seriesRequest,
                                               @AuthenticationPrincipal User user) {
        SeriesResponse newSeries = seriesService.createSeries(file, seriesRequest, user);
        return ResponseEntity
                .status(CREATED)
                .body(ResultResponse.create("시리즈 발행", newSeries));
    }

    @GetMapping("/{seriesId}")
    public ResponseEntity<Object> getBoardsInSeries(@PathVariable Long seriesId,
                                                    @AuthenticationPrincipal User user) {
        Series series = seriesService.getSeries(seriesId);
        List<SeriesBoard> seriesBoardList = seriesService.getBoardsInSeries(series);
        List<BoardResponse> boardResponseList = seriesService.entityToDto(seriesBoardList, user);
        Collections.reverse(boardResponseList); //최신순
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(seriesId + "번 시리즈 내의 게시글 목록 조회", boardResponseList));
    }

    @DeleteMapping("/{seriesId}")
    public ResponseEntity<Object> deleteSeries(@PathVariable Long seriesId,
                                               @AuthenticationPrincipal User user) {
        Series series = seriesService.getSeries(seriesId);
        seriesService.deleteSeries(series, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(seriesId + "번 시리즈 삭제"));
    }

    @PatchMapping("/add/{seriesId}/{boardId}")
    public ResponseEntity<Object> addBoardToSeries(@PathVariable Long seriesId,
                                                   @PathVariable Long boardId,
                                                   @AuthenticationPrincipal User user) {
        Series series = seriesService.getSeries(seriesId);
        Board board = boardService.getBoard(boardId);
        seriesService.addBoardToSeries(series, board, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(seriesId + "번 시리즈에 " + boardId + "번 게시글 추가"));
    }

    @PatchMapping("/{seriesId}")
    public ResponseEntity<Object> updateArchive(@PathVariable Long seriesId,
                                                @RequestBody Map<String, String> modRequest,
                                                @AuthenticationPrincipal User user) {
        Series series = seriesService.getSeries(seriesId);
        Series updatedSeries = seriesService.updateSeries(series, modRequest, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(seriesId + "번 시리즈 수정", updatedSeries));
    }

}