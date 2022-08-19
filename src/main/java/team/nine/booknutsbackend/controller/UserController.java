package team.nine.booknutsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.response.ResultResponse;
import team.nine.booknutsbackend.dto.response.UserProfileResponse;
import team.nine.booknutsbackend.dto.response.UserResponse;
import team.nine.booknutsbackend.service.DeleteUserService;
import team.nine.booknutsbackend.service.UserService;

import java.security.Principal;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final DeleteUserService deleteUserService;

    @GetMapping("/info")
    public ResponseEntity<Object> getUserInfoByToken(Principal principal) {
        UserResponse user = userService.getUserInfo(principal.getName());
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok("현재 로그인된 유저 정보 조회", user));
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<Object> getUserProfile(@PathVariable Long userId, Principal principal) {
        User me = userService.getUser(principal.getName());
        User target = userService.getUserById(userId);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(userId + "번 유저 프로필 조회", UserProfileResponse.of(me, target)));
    }

    @PatchMapping("/update/img")
    public ResponseEntity<Object> updateProfileImg(@RequestPart(value = "file", required = false) MultipartFile file, Principal principal) {
        User user = userService.getUser(principal.getName());
        UserResponse updatedUser = userService.updateProfileImg(file, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok("프로필 이미지 수정", updatedUser));
    }

    @PatchMapping("/update/password")
    public ResponseEntity<Object> updatePassword(@RequestBody Map<String, String> password, Principal principal) {
        User user = userService.getUser(principal.getName());
        userService.updatePassword(password, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok("비밀번호 변경"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteAccount(Principal principal) {
        User user = userService.getUser(principal.getName());
        deleteUserService.deleteAccount(user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(user.getUserId() + "번 유저 회원 탈퇴"));
    }

}