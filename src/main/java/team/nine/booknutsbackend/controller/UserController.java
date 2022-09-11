package team.nine.booknutsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.response.ResultResponse;
import team.nine.booknutsbackend.dto.response.UserProfileResponse;
import team.nine.booknutsbackend.dto.response.UserResponse;
import team.nine.booknutsbackend.service.DeleteUserService;
import team.nine.booknutsbackend.service.UserService;

import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final DeleteUserService deleteUserService;

    @GetMapping("/info")
    public ResponseEntity<Object> getUserInfoByToken(@AuthenticationPrincipal User user) {
        UserResponse userResponse = UserResponse.of(user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok("현재 로그인된 유저 정보 조회", userResponse));
    }

    @GetMapping("/profile/{nickname}")
    public ResponseEntity<Object> getUserProfile(@PathVariable String nickname,
                                                 @AuthenticationPrincipal User me) {
        User target = userService.getUserByNickname(nickname);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok("유저 '" + nickname + "'의 프로필 조회", UserProfileResponse.of(me, target)));
    }

    @PatchMapping("/update/img")
    public ResponseEntity<Object> updateProfileImg(@RequestPart(value = "file", required = false) MultipartFile file,
                                                   @AuthenticationPrincipal User user) {
        UserResponse updatedUser = userService.updateProfileImg(file, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok("프로필 이미지 수정", updatedUser));
    }

    @PatchMapping("/update/password")
    public ResponseEntity<Object> updatePassword(@RequestBody Map<String, String> password,
                                                 @AuthenticationPrincipal User user) {
        userService.updatePassword(password, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok("비밀번호 변경"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteAccount(@AuthenticationPrincipal User user) {
        deleteUserService.deleteAccount(user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(user.getUserId() + "번 유저 회원 탈퇴"));
    }

}