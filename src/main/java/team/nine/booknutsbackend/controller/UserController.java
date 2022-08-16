package team.nine.booknutsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.response.UserProfileResponse;
import team.nine.booknutsbackend.dto.response.UserResponse;
import team.nine.booknutsbackend.service.DeleteUserService;
import team.nine.booknutsbackend.service.UserService;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final DeleteUserService deleteUserService;

    //현재 유저 정보 - 토큰으로 조회
    @GetMapping("/info")
    public ResponseEntity<UserResponse> userInfoByHeaderToken(Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        return new ResponseEntity<>(UserResponse.userResponse(user), HttpStatus.OK);
    }

    //사용자 프로필 조회
    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable Long userId, Principal principal) {
        User curUser = userService.findUserByEmail(principal.getName());
        User targetUser = userService.findUserById(userId);
        return new ResponseEntity<>(UserProfileResponse.userProfileResponse(curUser, targetUser), HttpStatus.OK);
    }

    //프로필 이미지 수정
    @PatchMapping("/update/img")
    public ResponseEntity<UserResponse> updateProfileImg(@RequestPart(value = "file", required = false) MultipartFile file, Principal principal) {
        User originUser = userService.findUserByEmail(principal.getName());
        User updateUser = userService.updateProfileImg(file, originUser);
        return new ResponseEntity<>(UserResponse.userResponse(updateUser), HttpStatus.OK);
    }

    //비밀번호 재설정
    @PatchMapping("/update/password")
    public ResponseEntity<Object> updatePassword(@RequestBody Map<String, String> password, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        userService.updatePassword(password.get("oldPw"), password.get("newPw"), user);

        Map<String, String> map = new HashMap<>();
        map.put("result", "비밀번호 변경 완료");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    //회원 탈퇴
    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteAccount(Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        deleteUserService.deleteAccount(user);

        Map<String, String> map = new HashMap<>();
        map.put("result", "탈퇴 완료");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
