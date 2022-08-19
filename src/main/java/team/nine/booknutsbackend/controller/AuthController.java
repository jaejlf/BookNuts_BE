package team.nine.booknutsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.nine.booknutsbackend.dto.request.LoginRequest;
import team.nine.booknutsbackend.dto.request.SignUpRequest;
import team.nine.booknutsbackend.dto.response.AuthUserResponse;
import team.nine.booknutsbackend.dto.response.ResultResponse;
import team.nine.booknutsbackend.dto.response.TokenResponse;
import team.nine.booknutsbackend.dto.response.UserResponse;
import team.nine.booknutsbackend.service.EmailAuthService;
import team.nine.booknutsbackend.service.UserService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final EmailAuthService emailAuthService;

    @PostMapping("/join")
    public ResponseEntity<Object> join(@RequestPart(value = "file", required = false) MultipartFile file,
                                       @RequestPart("user") @Valid SignUpRequest signUpRequest) {
        UserResponse newUser = userService.join(file, signUpRequest);
        return ResponseEntity
                .status(CREATED)
                .body(ResultResponse.create("회원 가입", newUser));
    }

    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<Object> checkNicknameDuplicate(@PathVariable String nickname) {
        boolean result = userService.checkNicknameDuplicate(nickname);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok("닉네임 중복 체크 (false : 중복 X)", result));
    }

    @GetMapping("/loginId/{loginId}")
    public ResponseEntity<Object> checkUserIdDuplicate(@PathVariable String loginId) {
        boolean result = userService.checkLoginIdDuplicate(loginId);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok("로그인 아이디 중복 체크 (false : 중복 X)", result));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        AuthUserResponse user = userService.login(loginRequest);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok("로그인", user));
    }

    //토큰 재발급
    /*
     * access token 만료 시 401 에러 발생 -> 이 경우 refresh token으로 '/auth/refresh' 요청하여 토큰 재발급
     * refresh token도 만료되었을 경우(= 403 Forbidden) 재로그인 요청
     */
    @GetMapping("/refresh")
    public ResponseEntity<Object> tokenReIssue(HttpServletRequest request) {
        TokenResponse tokens = userService.tokenReIssue(request);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok("토큰 재발급", tokens));
    }

    @PostMapping("/email")
    public ResponseEntity<Object> sendEmail(@RequestBody Map<String, String> email) throws MessagingException {
        String authCode = emailAuthService.sendAuthCode(email.get("email"));
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok("이메일 인증 코드 발급", authCode));
    }

    @PostMapping("/confirm/code")
    public ResponseEntity<Object> confirmMailCode(@RequestBody Map<String, String> mailAuth) {
        boolean result = emailAuthService.confirmAuthCode(mailAuth.get("email"), mailAuth.get("code"));
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok("이메일 인증 코드 확인", result));
    }

}