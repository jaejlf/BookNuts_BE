package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.nine.booknutsbackend.config.JwtTokenProvider;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.request.LoginRequest;
import team.nine.booknutsbackend.dto.request.SignUpRequest;
import team.nine.booknutsbackend.dto.response.AuthUserResponse;
import team.nine.booknutsbackend.dto.response.TokenResponse;
import team.nine.booknutsbackend.dto.response.UserResponse;
import team.nine.booknutsbackend.exception.user.ExpiredRefreshTokenException;
import team.nine.booknutsbackend.exception.user.InvalidTokenException;
import team.nine.booknutsbackend.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static team.nine.booknutsbackend.exception.ErrorMessage.PASSWORD_ERROR;
import static team.nine.booknutsbackend.exception.ErrorMessage.USER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final CustomUserDetailService customUserDetailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AwsS3Service awsS3Service;

    @Transactional
    public UserResponse join(MultipartFile file, SignUpRequest signUpRequest) {
        User user = new User(
                signUpRequest,
                passwordEncoder.encode(signUpRequest.getPassword()),
                awsS3Service.uploadImg(file, signUpRequest.getNickname() + "-")
        );
        return UserResponse.of(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public boolean checkNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Transactional(readOnly = true)
    public boolean checkLoginIdDuplicate(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    @Transactional
    public AuthUserResponse login(LoginRequest loginRequest) {
        User user = getUser(loginRequest.getId());
        checkPasswordMatching(loginRequest.getPassword(), user);

        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        user.updateRefreshToken(refreshToken);
        return AuthUserResponse.of(userRepository.save(user), accessToken);
    }

    @Transactional
    public TokenResponse tokenReIssue(HttpServletRequest request) {
        String refreshToken = getRefreshToken(jwtTokenProvider.resolveToken(request));
        User user = getUserByToken(refreshToken);

        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail());
        refreshToken = getReIssuedRefreshToken(refreshToken, user);
        return TokenResponse.of(accessToken, refreshToken);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserInfo(String id) {
        User user = getUser(id);
        return UserResponse.of(user);
    }

    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return getUser(userId);
    }

    @Transactional
    public UserResponse updateProfileImg(MultipartFile file, User user) {
        awsS3Service.deleteImg(user.getProfileImgUrl());  //기존 이미지 버킷에서 삭제
        user.updateProfileImgUrl(awsS3Service.uploadImg(file, user.getNickname() + "-"));
        return UserResponse.of(userRepository.save(user));
    }

    @Transactional
    public void updatePassword(Map<String, String> password, User user) {
        String oldPw = password.get("oldPw");
        String newPw = password.get("newPw");

        checkPasswordMatching(oldPw, user);
        user.updatePassword(passwordEncoder.encode(newPw));
        userRepository.save(user);
    }

    private User getUser(String id) {
        return customUserDetailService.loadUserByUsername(id);
    }

    private void checkPasswordMatching(String inputPw, User user) {
        if (!passwordEncoder.matches(inputPw, user.getPassword())) {
            throw new IllegalArgumentException(PASSWORD_ERROR.getMsg());
        }
    }

    private User getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.getMsg()));
        if (!user.isEnabled()) throw new UsernameNotFoundException(USER_NOT_FOUND.getMsg());
        return user;
    }

    private User getUserByToken(String refreshToken) {
        return userRepository.findByRefreshToken(refreshToken).orElseThrow(InvalidTokenException::new);
    }

    private String getRefreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new ExpiredRefreshTokenException();
        }
        return refreshToken;
    }

    private String getReIssuedRefreshToken(String refreshToken, User user) {
        long validTime = jwtTokenProvider.getValidTime(refreshToken);
        if (validTime <= 172800000) { //refresh token 만료 기간 체크 -> 2일 이하로 남은 경우 재발급
            refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());
            user.updateRefreshToken(refreshToken);
            userRepository.save(user);
        }
        return refreshToken;
    }

}