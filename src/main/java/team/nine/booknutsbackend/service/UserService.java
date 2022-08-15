package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.nine.booknutsbackend.config.JwtTokenProvider;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.exception.user.InvalidTokenException;
import team.nine.booknutsbackend.exception.user.PasswordErrorException;
import team.nine.booknutsbackend.exception.user.UserNotFoundException;
import team.nine.booknutsbackend.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final CustomUserDetailService customUserDetailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AwsS3Service awsS3Service;

    //회원가입
    @Transactional
    public User join(MultipartFile file, User user) {
        user.setProfileImgUrl(awsS3Service.uploadImg(file, user.getNickname() + "-"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    //로그인
    @Transactional
    public User login(String id, String password) {
        User user = findUserByEmail(id);
        if (!passwordEncoder.matches(password, user.getPassword())) throw new PasswordErrorException();

        user.setRefreshToken(jwtTokenProvider.createRefreshToken(user.getEmail()));
        return userRepository.save(user);
    }

    //ID로 유저 정보 조회
    @Transactional(readOnly = true)
    public User findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        if (!user.isEnabled()) throw new UserNotFoundException();

        return user;
    }

    //이메일 또는 로그인 아이디로 유저 정보 조회 (UserDetailService - loadUserByUsername)
    @Transactional(readOnly = true)
    public User findUserByEmail(String id) {
        return customUserDetailService.loadUserByUsername(id);
    }

    //유저 닉네임 중복 체크
    @Transactional(readOnly = true)
    public boolean checkNicknameDuplication(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    //유저 로그인 아이디 중복 체크
    @Transactional(readOnly = true)
    public boolean checkLoginIdDuplication(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    //토큰 재발급
    @Transactional
    public Object tokenReIssue(String refreshToken) {
        User user = userRepository.findByRefreshToken(refreshToken) //db에 해당 refresh token이 존재하지 않는 경우
                .orElseThrow(InvalidTokenException::new);
        String accessToken = jwtTokenProvider.createAccessToken(user.getUsername());

        //refresh token 만료 기간 체크 -> 2일 이하로 남은 경우 재발급
        long validTime = jwtTokenProvider.getValidTime(refreshToken);
        if (validTime <= 172800000) {
            refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());
            user.setRefreshToken(refreshToken);
            userRepository.save(user);
        }

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", accessToken);
        map.put("refreshToken", refreshToken);

        return map;
    }

    //프로필 이미지 업데이트
    @Transactional
    public User updateProfileImg(MultipartFile file, User user) {
        awsS3Service.deleteImg(user.getProfileImgUrl());  //기존 이미지 버킷에서 삭제
        user.setProfileImgUrl(awsS3Service.uploadImg(file, "profile-"));
        return userRepository.save(user);
    }

    //비밀번호 재설정
    @Transactional
    public void updatePassword(String oldPw, String newPw, User user) {
        if (!passwordEncoder.matches(oldPw, user.getPassword())) throw new PasswordErrorException();
        user.setPassword(passwordEncoder.encode(newPw));
        userRepository.save(user);
    }

}