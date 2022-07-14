package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.exception.user.UserNotFoundException;
import team.nine.booknutsbackend.repository.UserRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    //이메일 또는 로그인 아이디로 유저 정보 조회
    @Transactional(readOnly = true)
    @Override
    public User loadUserByUsername(String id) {
        String regx = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(id);

        User user;
        if (matcher.matches()) user = userRepository.findByEmail(id).orElseThrow(UserNotFoundException::new);
        else user = userRepository.findByLoginId(id).orElseThrow(UserNotFoundException::new);
        if (!user.isEnabled()) throw new UserNotFoundException();

        return user;
    }

}
