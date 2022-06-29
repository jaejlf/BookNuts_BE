package team.nine.booknutsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.response.FollowResponse;
import team.nine.booknutsbackend.service.FollowService;
import team.nine.booknutsbackend.service.UserService;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/follow")
public class FollowController {

    private final FollowService followService;
    private final UserService userService;

    //팔로우
    @PutMapping("/{followingId}")
    public ResponseEntity<Object> follow(@PathVariable Long followingId, Principal principal) {
        User follower = userService.findUserByEmail(principal.getName());
        User following = userService.findUserById(followingId);
        followService.follow(following, follower);

        Map<String, String> map = new HashMap<>();
        map.put("result", "팔로우 완료");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    //언팔로우
    @DeleteMapping("/{unfollowingId}")
    public ResponseEntity<Object> unfollow(@PathVariable Long unfollowingId, Principal principal) {
        User follower = userService.findUserByEmail(principal.getName());    //나(팔로워)
        User unfollowing = userService.findUserById(unfollowingId);
        followService.unfollow(unfollowing, follower);

        Map<String, String> map = new HashMap<>();
        map.put("result", "언팔로우 완료");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    //팔로잉 리스트
    @GetMapping("/followinglist/{userId}")
    public ResponseEntity<List<FollowResponse>> findMyFollowingList(@PathVariable Long userId, Principal principal) {
        User currentLoginId = userService.findUserByEmail(principal.getName());
        User user = userService.findUserById(userId);
        return new ResponseEntity<>(followService.getMyFollowingList(user), HttpStatus.OK);
    }

    //팔로워 리스트
    @GetMapping("/followerlist/{userId}")
    public ResponseEntity<List<FollowResponse>> findMyFollowerList(@PathVariable Long userId, Principal principal) {
        User currentLoginId = userService.findUserByEmail(principal.getName());
        User user = userService.findUserById(userId);
        return new ResponseEntity<>(followService.getMyFollowerList(user), HttpStatus.OK);
    }

}
