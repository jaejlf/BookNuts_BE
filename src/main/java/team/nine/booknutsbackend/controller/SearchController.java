package team.nine.booknutsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.response.BoardResponse;
import team.nine.booknutsbackend.dto.response.DebateRoomResponse;
import team.nine.booknutsbackend.dto.response.ResultResponse;
import team.nine.booknutsbackend.dto.response.UserProfileResponse;
import team.nine.booknutsbackend.service.SearchService;
import team.nine.booknutsbackend.service.UserService;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;
    private final UserService userService;

    @GetMapping("/board")
    public ResponseEntity<Object> searchBoard(@RequestParam String keyword, Principal principal) {
        User user = userService.getUser(principal.getName());
        List<BoardResponse> boardSearchResultList = searchService.searchBoard(keyword, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok("'" + keyword + "' 검색 결과 (게시글)", boardSearchResultList));
    }

    @GetMapping("/room")
    public ResponseEntity<Object> searchRoom(@RequestParam String keyword) {
        List<DebateRoomResponse> debateSearchResultList = searchService.searchRoom(keyword);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok("'" + keyword + "' 검색 결과 (토론방)", debateSearchResultList));
    }

    @GetMapping("/user")
    public ResponseEntity<Object> searchUser(@RequestParam String keyword, Principal principal) {
        User user = userService.getUser(principal.getName());
        List<UserProfileResponse> userSearchResultList = searchService.searchUser(keyword, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok("'" + keyword + "' 검색 결과 (유저)", userSearchResultList));
    }

}