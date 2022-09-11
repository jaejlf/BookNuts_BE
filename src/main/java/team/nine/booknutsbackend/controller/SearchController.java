package team.nine.booknutsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.domain.debate.DebateRoom;
import team.nine.booknutsbackend.dto.response.BoardResponse;
import team.nine.booknutsbackend.dto.response.DebateRoomResponse;
import team.nine.booknutsbackend.dto.response.ResultResponse;
import team.nine.booknutsbackend.dto.response.UserProfileResponse;
import team.nine.booknutsbackend.service.SearchService;

import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/board")
    public ResponseEntity<Object> searchBoard(@RequestParam String keyword,
                                              @AuthenticationPrincipal User user) {
        List<Board> boardList = searchService.searchBoard(keyword);
        List<BoardResponse> boardResponseList = searchService.entityToDto(boardList, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok("'" + keyword + "' 검색 결과 (게시글)", boardResponseList));
    }

    @GetMapping("/room")
    public ResponseEntity<Object> searchRoom(@RequestParam String keyword) {
        List<DebateRoom> debateRoomList = searchService.searchRoom(keyword);
        List<DebateRoomResponse> debateRoomResponseList = searchService.entityToDto(debateRoomList);
        Collections.reverse(debateRoomResponseList); //최신순
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok("'" + keyword + "' 검색 결과 (토론방)", debateRoomResponseList));
    }

    @GetMapping("/user")
    public ResponseEntity<Object> searchUser(@RequestParam String keyword,
                                             @AuthenticationPrincipal User user) {
        List<User> userList = searchService.searchUser(keyword);
        List<UserProfileResponse> userProfileResponseList = searchService.entityToDto(user, userList);
        Collections.reverse(userProfileResponseList); //최신순
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok("'" + keyword + "' 검색 결과 (유저)", userProfileResponseList));
    }

}