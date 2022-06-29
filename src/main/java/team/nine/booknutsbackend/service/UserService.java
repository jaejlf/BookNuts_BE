package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.nine.booknutsbackend.config.JwtTokenProvider;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.exception.user.PasswordErrorException;
import team.nine.booknutsbackend.repository.UserRepository;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final CustomUserDetailService customUserDetailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    //회원가입
    @Transactional
    public User join(User user) {
        String accessToken = jwtTokenProvider.createAccessToken(user.getUsername(), user.getRoles());
        user.setAccessToken(accessToken);

        String rawPw = user.getPassword();
        user.setPassword(passwordEncoder.encode(rawPw));

        return userRepository.save(user);
    }

    //로그인
    //refresh token 관련 추가 구현 필요
    @Transactional
    public User login(User user, String inputPassword) {
        if (!passwordEncoder.matches(inputPassword, user.getPassword())) {
            throw new PasswordErrorException("잘못된 비밀번호입니다.");
        }

        String refreshToken = jwtTokenProvider.createRefreshToken();
        user.setRefreshToken(refreshToken);

        return userRepository.save(user);
    }

    //ID로 유저 정보 조회
    @Transactional(readOnly = true)
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저의 아이디입니다."));
    }

    //메일로 유저 정보 조회 (UserDetailService - loadUserByUsername)
    @Transactional(readOnly = true)
    public User findUserByEmail(String email) {
        return customUserDetailService.loadUserByUsername(email);
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

}