package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.nine.booknutsbackend.domain.User;
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
        if (matcher.matches()) user = userRepository.findByEmail(id)
                .orElseThrow(() -> new UsernameNotFoundException("가입되지 않은 이메일입니다."));
        else user = userRepository.findByLoginId(id)
                .orElseThrow(() -> new UsernameNotFoundException("가입되지 않은 아이디입니다."));

        if (!user.isEnabled()) throw new UsernameNotFoundException("사용할 수 없는 계정입니다.");
        return user;
    }

}
