package team.nine.booknutsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.response.FollowResponse;
import team.nine.booknutsbackend.dto.response.ResultResponse;
import team.nine.booknutsbackend.service.FollowService;
import team.nine.booknutsbackend.service.UserService;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/follow")
public class FollowController {

    private final FollowService followService;
    private final UserService userService;

    @PutMapping("/{targetNickname}")
    public ResponseEntity<Object> clickFollow(@PathVariable String targetNickname,
                                              @AuthenticationPrincipal User me) {
        User target = userService.getUserByNickname(targetNickname);
        String result = followService.clickFollow(me, target);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok("유저 '" + targetNickname + "' " + result));
    }

    @GetMapping("/followinglist/{nickname}")
    public ResponseEntity<Object> getFollowingList(@PathVariable String nickname) {
        User user = userService.getUserByNickname(nickname);
        List<FollowResponse> followingList = followService.getFollowingList(user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok("유저 '" + nickname + "'의 팔로잉 목록", followingList));
    }

    @GetMapping("/followerlist/{nickname}")
    public ResponseEntity<Object> findMyFollowerList(@PathVariable String nickname) {
        User user = userService.getUserByNickname(nickname);
        List<FollowResponse> followerList = followService.getMyFollowerList(user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok("유저 '" + nickname + "'의 팔로워 목록", followerList));
    }

}