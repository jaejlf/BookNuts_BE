package team.nine.booknutsbackend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User 서비스")
class UserServiceTest extends CommonServiceTest {

    @Autowired
    private UserService userService;

    @DisplayName("닉네임 중복 체크")
    @ParameterizedTest
    @CsvSource({"테스터, true", "팅팅팅, false"})
    void checkNicknameDuplicate(String nickname, boolean expected) {
        //given & when
        boolean result = userService.checkNicknameDuplicate(nickname);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("로그인 아이디 중복 체크")
    @ParameterizedTest
    @CsvSource({"tester, true", "xxxxx, false"})
    void checkLoginIdDuplicate(String loginId, boolean expected) {
        //given & when
        boolean result = userService.checkLoginIdDuplicate(loginId);

        //then
        assertThat(result).isEqualTo(expected);
    }

}