package team.nine.booknutsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.nine.booknutsbackend.config.JwtTokenProvider;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.response.UserProfileResponse;
import team.nine.booknutsbackend.dto.response.UserResponse;
import team.nine.booknutsbackend.service.UserService;
import team.nine.booknutsbackend.service.FollowService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final FollowService followService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    //현재 유저 정보 - 토큰으로 조회
    @GetMapping("/info")
    public ResponseEntity<Object> userInfoByHeaderToken(HttpServletRequest request, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        String accessToken = jwtTokenProvider.resolveToken(request);
        return new ResponseEntity<>(UserResponse.userResponse(user, accessToken), HttpStatus.OK);
    }

    //사용자 프로필 조회
    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable Long userId, Principal principal) {
        User curUser = userService.findUserByEmail(principal.getName());
        User targetUser = userService.findUserById(userId);
        return new ResponseEntity<>(UserProfileResponse.userProfileResponse(curUser, targetUser), HttpStatus.OK);
    }

}
