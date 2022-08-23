package team.nine.booknutsbackend.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import team.nine.booknutsbackend.config.JwtTokenProvider;
import team.nine.booknutsbackend.domain.Board;
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
    @Mock BoardRepository boardRepository;
    @Mock FollowRepository followRepository;

    User user = user();
    Board board = board();

    private User user() {
        return new User(
                "nutstnuts",
                "$$ENCODED_PASSWORD$$",
                "김넛츠",
                "콩자반",
                "nuts@naver.com",
                ""
        );
    }

    private Board board() {
        return new Board(
                "'땅콩은 콩일까'를 읽고",
                "땅콩이 콩인지 아닌지 궁금해졌다.",
                "땅콩은 콩일까?",
                "콩작가",
                "www.imgurl...",
                "독립서적",
                user
        );
    }

}