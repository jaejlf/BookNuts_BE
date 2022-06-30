package team.nine.booknutsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.nine.booknutsbackend.config.JwtTokenProvider;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.request.UserRequest;
import team.nine.booknutsbackend.dto.response.UserResponse;
import team.nine.booknutsbackend.exception.user.InvalidTokenException;
import team.nine.booknutsbackend.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    //회원가입
    @PostMapping("/join")
    public ResponseEntity<Object> join(@RequestBody @Valid UserRequest user) {
        User newUser = userService.join(UserRequest.userRequest(user));
        return new ResponseEntity<>(UserResponse.userResponse(newUser, ""), HttpStatus.CREATED);
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
        String accessToken = jwtTokenProvider.createAccessToken(loginUser.getUsername(), loginUser.getRoles());
        return new ResponseEntity<>(UserResponse.userResponse(loginUser, accessToken), HttpStatus.OK);
    }

    //토큰 재발급
    /*
     * access token 만료 시 401 에러 발생 -> 이 경우 refresh token으로 '/auth/refresh' 요청하여 토큰 재발급
     * refresh token도 만료되었을 경우(= 403 Forbidden) 재로그인 요청
     */
    @GetMapping("/refresh")
    public ResponseEntity<Object> refreshToken(HttpServletRequest request) throws InvalidTokenException {
        String refreshToken = jwtTokenProvider.resolveToken(request);
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new InvalidTokenException("오랜 시간 사용하지 않아, 토큰이 만료되었습니다.");
        }

        return new ResponseEntity<>(userService.tokenReIssue(refreshToken), HttpStatus.OK);
    }

}