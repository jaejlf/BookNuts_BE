package team.nine.booknutsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.request.UserRequest;
import team.nine.booknutsbackend.service.UserService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    //회원가입
    @PostMapping("/join")
    public ResponseEntity<Object> join(@RequestBody @Valid UserRequest user) {
        User newUser = userService.join(UserRequest.userRequest(user));
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    //유저 닉네임 중복 체크
    @GetMapping("/checkNickname/{nickname}")
    public ResponseEntity<Boolean> checkNicknameDuplicate(@PathVariable String nickname) {
        return ResponseEntity.ok(userService.checkNicknameDuplication(nickname));
    }

    //유저 로그인 아이디 중복 체크
    @GetMapping("/checkLoginId/{loginId}")
    public ResponseEntity<Boolean> checkUserIdDuplicate(@PathVariable String loginId) {
        return ResponseEntity.ok(userService.checkLoginIdDuplication(loginId));
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserRequest user) {
        User loginUser = userService.login(userService.findUserByEmail(user.getEmail()), user.getPassword());
        return new ResponseEntity<>(loginUser, HttpStatus.OK);
    }

}