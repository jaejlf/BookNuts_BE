package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.nine.booknutsbackend.config.JwtTokenProvider;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.request.LoginRequest;
import team.nine.booknutsbackend.dto.request.ReissueRequest;
import team.nine.booknutsbackend.dto.request.SignUpRequest;
import team.nine.booknutsbackend.dto.response.AuthUserResponse;
import team.nine.booknutsbackend.dto.response.TokenResponse;
import team.nine.booknutsbackend.dto.response.UserResponse;
import team.nine.booknutsbackend.exception.user.ExpiredRefreshTokenException;
import team.nine.booknutsbackend.exception.user.InvalidTokenException;
import team.nine.booknutsbackend.repository.UserRepository;

import java.util.Map;
import java.util.Objects;

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
    private final RedisService redisService;

    @Transactional
    public UserResponse join(MultipartFile file, SignUpRequest signUpRequest) {
        User user = new User(
                signUpRequest.getLoginId(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                signUpRequest.getUsername(),
                signUpRequest.getNickname(),
                signUpRequest.getEmail(),
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
        User user = customUserDetailService.loadUserByUsername(loginRequest.getId());
        checkPasswordMatching(loginRequest.getPassword(), user);

        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        return AuthUserResponse.of(user, accessToken, refreshToken);
    }

    @Transactional
    public TokenResponse tokenReIssue(ReissueRequest reissueRequest) {
        User user = getUserByEmail(reissueRequest.getEmail());
        String refreshToken = getCheckedRefreshToken(reissueRequest, user);
        String accessToken = jwtTokenProvider.createAccessToken(reissueRequest.getEmail());
        return TokenResponse.of(accessToken, refreshToken);
    }

    @Transactional(readOnly = true)
    @Cacheable(key = "#nickname", value = "getUserByNickname")
    public User getUserByNickname(String nickname) {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.getMsg()));
        if (!user.isEnabled()) throw new UsernameNotFoundException(USER_NOT_FOUND.getMsg());
        return user;
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

    private void checkPasswordMatching(String inputPw, User user) {
        if (!passwordEncoder.matches(inputPw, user.getPassword())) {
            throw new IllegalArgumentException(PASSWORD_ERROR.getMsg());
        }
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.getMsg()));
    }

    private String getCheckedRefreshToken(ReissueRequest tokenRequest, User user) {

        //리프레쉬 토큰 유효성 체크
        String refreshToken = tokenRequest.getRefreshToken();
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new ExpiredRefreshTokenException();
        }

        //DB에 저장된 refresh 토큰과 일치하는지 체크
        String email = tokenRequest.getEmail();
        String storedRefreshToken = redisService.getValues("token-" + user.getEmail());
        if (!Objects.equals(storedRefreshToken, refreshToken)) {
            throw new InvalidTokenException();
        }

        //토큰 만료 기간이 2일 이내로 남았을 경우 재발급
        Long remainTime = jwtTokenProvider.getValidTime(refreshToken);
        if (remainTime <= 172800000) {
            refreshToken = jwtTokenProvider.createRefreshToken(email);
        }
        return refreshToken;

    }

}