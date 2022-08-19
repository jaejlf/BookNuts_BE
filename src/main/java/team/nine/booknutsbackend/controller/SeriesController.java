package team.nine.booknutsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.domain.series.Series;
import team.nine.booknutsbackend.dto.request.SeriesRequest;
import team.nine.booknutsbackend.dto.response.BoardResponse;
import team.nine.booknutsbackend.dto.response.ResultResponse;
import team.nine.booknutsbackend.dto.response.SeriesResponse;
import team.nine.booknutsbackend.service.SeriesService;
import team.nine.booknutsbackend.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
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

    @GetMapping("/list/{userId}")
    public ResponseEntity<Object> getSeriesList(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        List<SeriesResponse> seriesList = seriesService.getSeriesList(user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(userId + "번 유저의 시리즈 목록 조회", seriesList));
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createSeries(@RequestPart(value = "file", required = false) MultipartFile file,
                                               @RequestPart(value = "series") @Valid SeriesRequest seriesRequest, Principal principal) {
        User user = userService.getUser(principal.getName());
        SeriesResponse newSeries = seriesService.createSeries(file, seriesRequest, user);
        return ResponseEntity
                .status(CREATED)
                .body(ResultResponse.create("시리즈 발행", newSeries));
    }

    @GetMapping("/{seriesId}")
    public ResponseEntity<Object> getBoardsInSeries(@PathVariable Long seriesId, Principal principal) {
        User user = userService.getUser(principal.getName());
        List<BoardResponse> seriesBoardList = seriesService.getBoardsInSeries(seriesId, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(seriesId + "번 시리즈 내의 게시글 목록 조회", seriesBoardList));
    }

    @DeleteMapping("/{seriesId}")
    public ResponseEntity<Object> deleteSeries(@PathVariable Long seriesId, Principal principal) {
        User user = userService.getUser(principal.getName());
        seriesService.deleteSeries(seriesId, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(seriesId + "번 시리즈 삭제"));
    }

    @PatchMapping("/add/{seriesId}/{boardId}")
    public ResponseEntity<Object> addBoardToSeries(@PathVariable Long seriesId,
                                                   @PathVariable Long boardId, Principal principal) {
        User user = userService.getUser(principal.getName());
        seriesService.addBoardToSeries(seriesId, boardId, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(seriesId + "번 시리즈에 " + boardId + "번 게시글 추가"));
    }

    @PatchMapping("/{seriesId}")
    public ResponseEntity<Object> updateArchive(@PathVariable Long seriesId,
                                                @RequestBody Map<String, String> modRequest, Principal principal) {
        User user = userService.getUser(principal.getName());
        Series updatedSeries = seriesService.updateSeries(seriesId, modRequest, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(seriesId + "번 시리즈 수정", updatedSeries));
    }

}