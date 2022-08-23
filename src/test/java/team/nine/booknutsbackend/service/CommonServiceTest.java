package team.nine.booknutsbackend.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import team.nine.booknutsbackend.config.JwtTokenProvider;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.repository.BoardRepository;
import team.nine.booknutsbackend.repository.FollowRepository;
import team.nine.booknutsbackend.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class CommonServiceTest {

    @Mock UserRepository userRepository;
    @Mock CustomUserDetailService customUserDetailService;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtTokenProvider jwtTokenProvider;
    @Mock AwsS3Service awsS3Service;
    @Mock RedisService redisService;

    User user = booknutsUser();

    private User booknutsUser() {
        return new User(
                "nutstnuts",
                "$$ENCODED_PASSWORD$$",
                "김넛츠",
                "콩자반",
                "nuts@naver.com",
                ""
        );
    }

}
