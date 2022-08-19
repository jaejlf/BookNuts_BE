package team.nine.booknutsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.response.FollowResponse;
import team.nine.booknutsbackend.dto.response.ResultResponse;
import team.nine.booknutsbackend.service.FollowService;
import team.nine.booknutsbackend.service.UserService;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/follow")
public class FollowController {

    private final FollowService followService;
    private final UserService userService;

    @PutMapping("/{followingId}")
    public ResponseEntity<Object> follow(@PathVariable Long followingId, Principal principal) {
        User me = userService.getUser(principal.getName());
        User target = userService.getUserById(followingId);
        followService.follow(me, target);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(followingId + "번 유저 팔로우"));
    }

    @DeleteMapping("/{unfollowingId}")
    public ResponseEntity<Object> unfollow(@PathVariable Long unfollowingId, Principal principal) {
        User me = userService.getUser(principal.getName());
        User target = userService.getUserById(unfollowingId);
        followService.unfollow(me, target);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(unfollowingId + "번 유저 언팔로우"));
    }

    @GetMapping("/followinglist/{userId}")
    public ResponseEntity<Object> getFollowingList(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        List<FollowResponse> followingList = followService.getFollowingList(user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(userId + "번 유저의 팔로잉 목록", followingList));
    }

    @GetMapping("/followerlist/{userId}")
    public ResponseEntity<Object> findMyFollowerList(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        List<FollowResponse> followerList = followService.getMyFollowerList(user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(userId + "번 유저의 팔로워 목록", followerList));
    }

}