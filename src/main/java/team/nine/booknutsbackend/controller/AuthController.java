package team.nine.booknutsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.nine.booknutsbackend.config.JwtTokenProvider;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.request.UserRequest;
import team.nine.booknutsbackend.dto.response.LoginResponse;
import team.nine.booknutsbackend.dto.response.UserResponse;
import team.nine.booknutsbackend.exception.user.ExpiredRefreshTokenException;
import team.nine.booknutsbackend.service.EmailAuthService;
import team.nine.booknutsbackend.service.UserService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final EmailAuthService emailAuthService;
    private final JwtTokenProvider jwtTokenProvider;

    //회원가입
    @PostMapping("/join")
    public ResponseEntity<UserResponse> join(@RequestPart(value = "file", required = false) MultipartFile file,
                                             @RequestPart("user") @Valid UserRequest user) {
        User newUser = userService.join(file, UserRequest.userRequest(user));
        return new ResponseEntity<>(UserResponse.userResponse(newUser), HttpStatus.CREATED);
    }

    //유저 닉네임 중복 체크
    @GetMapping("/checkNickname/{nickname}")
    public ResponseEntity<Boolean> checkNicknameDuplicate(@PathVariable String nickname) {
        return ResponseEntity.ok(userService.checkNicknameDuplication(nickname));
    }

    //401 인증 실패, 404 사용자 없음
    @PostMapping("/sendEmail")
    public ResponseEntity<Object> sendEmail(@RequestBody Map<String, String> userEmail) throws MessagingException {
        return ResponseEntity.ok(emailAuthService.sendSimpleMessage(userEmail.get("email")));
    }

    @PostMapping("/confirmEmailCode")
    public ResponseEntity<Object> confirmMailCode(@RequestBody Map<String, String> mailcode) {
        return ResponseEntity.ok(emailAuthService.confirmEmailCode(mailcode.get("email"), mailcode.get("code")));
    }

    //유저 로그인 아이디 중복 체크
    @GetMapping("/checkLoginId/{loginId}")
    public ResponseEntity<Boolean> checkUserIdDuplicate(@PathVariable String loginId) {
        return ResponseEntity.ok(userService.checkLoginIdDuplication(loginId));
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String, String> user) {
        User loginUser = userService.login(user.get("id"), user.get("password"));
        String accessToken = jwtTokenProvider.createAccessToken(loginUser.getUsername());
        return new ResponseEntity<>(LoginResponse.loginResponse(loginUser, accessToken), HttpStatus.OK);
    }

    //토큰 재발급
    /*
     * access token 만료 시 401 에러 발생 -> 이 경우 refresh token으로 '/auth/refresh' 요청하여 토큰 재발급
     * refresh token도 만료되었을 경우(= 403 Forbidden) 재로그인 요청
     */
    @GetMapping("/refresh")
    public ResponseEntity<Object> refreshToken(HttpServletRequest request) {
        String refreshToken = jwtTokenProvider.resolveToken(request);
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new ExpiredRefreshTokenException();
        }

        return new ResponseEntity<>(userService.tokenReIssue(refreshToken), HttpStatus.OK);
    }

}