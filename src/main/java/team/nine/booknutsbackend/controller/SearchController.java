package team.nine.booknutsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.response.BoardResponse;
import team.nine.booknutsbackend.dto.response.DebateRoomResponse;
import team.nine.booknutsbackend.dto.response.UserProfileResponse;
import team.nine.booknutsbackend.service.SearchService;
import team.nine.booknutsbackend.service.UserService;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;
    private final UserService userService;

    @GetMapping("/board")
    public ResponseEntity<List<BoardResponse>> searchBoard(@RequestParam String keyword, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        return new ResponseEntity<>(searchService.searchBoard(keyword, user), HttpStatus.OK);
    }

    @GetMapping("/room")
    public ResponseEntity<List<DebateRoomResponse>> searchRoom(@RequestParam String keyword) {
        return new ResponseEntity<>(searchService.searchRoom(keyword), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserProfileResponse>> searchUser(@RequestParam String keyword, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        return new ResponseEntity<>(searchService.searchUser(keyword, user), HttpStatus.OK);
    }

}
