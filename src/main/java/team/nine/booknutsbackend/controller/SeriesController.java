package team.nine.booknutsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.domain.series.Series;
import team.nine.booknutsbackend.dto.request.SeriesRequest;
import team.nine.booknutsbackend.dto.response.BoardResponse;
import team.nine.booknutsbackend.dto.response.SeriesResponse;
import team.nine.booknutsbackend.service.SeriesService;
import team.nine.booknutsbackend.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/series")
public class SeriesController {

    private final SeriesService seriesService;
    private final UserService userService;

    //특정 유저의 시리즈 목록 조회
    @GetMapping("/list/{userId}")
    public ResponseEntity<List<SeriesResponse>> getSeriesList(@PathVariable Long userId) {
        User owner = userService.findUserById(userId);
        return new ResponseEntity<>(seriesService.getSeriesList(owner), HttpStatus.OK);
    }

    //시리즈 발행
    @PostMapping("/create")
    public ResponseEntity<SeriesResponse> createSeries(@RequestPart(value = "file", required = false) MultipartFile file,
                                                       @RequestPart(value = "series") @Valid SeriesRequest seriesRequest, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        Series newSeries = seriesService.createSeries(file, SeriesRequest.seriesRequest(seriesRequest, user), seriesRequest.getBoardIdlist());
        return new ResponseEntity<>(SeriesResponse.seriesResponse(newSeries), HttpStatus.CREATED);
    }

    //특정 시리즈 내의 게시글 조회
    @GetMapping("/{seriesId}")
    public ResponseEntity<List<BoardResponse>> getSeriesBoards(@PathVariable Long seriesId, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        return new ResponseEntity<>(seriesService.getSeriesBoards(seriesId, user), HttpStatus.OK);
    }

    //시리즈 삭제
    @DeleteMapping("/{seriesId}")
    public ResponseEntity<Object> deleteSeries(@PathVariable Long seriesId, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        seriesService.deleteSeries(seriesId, user);

        Map<String, String> map = new HashMap<>();
        map.put("result", "삭제 완료");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    //시리즈에 게시글 추가
    @PatchMapping("/add/{seriesId}/{boardId}")
    public ResponseEntity<Object> addPostToSeries(@PathVariable Long seriesId, @PathVariable Long boardId, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        seriesService.addPostToSeries(seriesId, boardId, user);

        Map<String, String> map = new HashMap<>();
        map.put("result", "추가 완료");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    //시리즈 수정
    @PatchMapping("/{seriesId}")
    public ResponseEntity<SeriesResponse> updateArchive(@PathVariable Long seriesId, @RequestBody SeriesRequest seriesRequest, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        Series updateSeries = seriesService.updateSeries(seriesId, seriesRequest, user);
        return new ResponseEntity<>(SeriesResponse.seriesResponse(updateSeries), HttpStatus.OK);
    }

}
