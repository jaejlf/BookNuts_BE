package team.nine.booknutsbackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.repository.BoardRepository;
import team.nine.booknutsbackend.repository.UserRepository;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
public class CommonServiceTest {

    @Autowired BoardRepository boardRepository;
    @Autowired UserRepository userRepository;
    @Autowired PasswordEncoder passwordEncoder;

    User user;

    @BeforeEach
    void setUp() {
        user = getUser();
        userRepository.save(user);
    }

    private User getUser() {
        return new User(
                "tester",
                passwordEncoder.encode("{{RAW_PASSWORD}}"),
                "테스터",
                "테스터",
                "tester@nuts.com",
                ""
        );
    }

}