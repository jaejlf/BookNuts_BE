package team.nine.booknutsbackend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import team.nine.booknutsbackend.dto.request.LoginRequest;
import team.nine.booknutsbackend.dto.request.ReissueRequest;
import team.nine.booknutsbackend.dto.request.SignUpRequest;
import team.nine.booknutsbackend.dto.response.AuthUserResponse;
import team.nine.booknutsbackend.dto.response.TokenResponse;
import team.nine.booknutsbackend.dto.response.UserResponse;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("User 서비스")
class UserServiceTest extends CommonServiceTest {

    @InjectMocks
    private UserService userService;

    @DisplayName("회원가입 성공")
    @Test
    void join() {
        //given
        SignUpRequest signUpRequest = new SignUpRequest(
                "nutstnuts",
                "@@RAW_PASSWORD@@",
                "김넛츠",
                "콩자반",
                "nuts@naver.com",
                ""
        );

        given(passwordEncoder.encode(any())).willReturn("$$ENCODED_PASSWORD$$");
        given(awsS3Service.uploadImg(any(), any())).willReturn("");
        given(userRepository.save(any())).willReturn(user);

        //when
        UserResponse result = userService.join(null, signUpRequest);

        //then
        UserResponse expected = UserResponse.of(user);
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("닉네임 중복 체크")
    @ParameterizedTest
    @CsvSource({"콩자반, true", "팅팅팅, false"})
    void checkNicknameDuplicate(String nickname, boolean expected) {
        //given
        given(userRepository.existsByNickname(nickname)).willReturn(expected);

        //when
        boolean result = userService.checkNicknameDuplicate(nickname);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("로그인 아이디 중복 체크")
    @ParameterizedTest
    @CsvSource({"nutstnuts, true", "bookbook, false"})
    void checkLoginIdDuplicate(String loginId, boolean expected) {
        //given
        given(userRepository.existsByLoginId(loginId)).willReturn(expected);

        //when
        boolean result = userService.checkLoginIdDuplicate(loginId);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("로그인 성공")
    @Test
    void login() {
        //given
        LoginRequest loginRequest = new LoginRequest(
                "nutstnuts",
                "12341234!"
        );

        given(customUserDetailService.loadUserByUsername(any())).willReturn(user);
        given(jwtTokenProvider.createAccessToken(any())).willReturn("MOCK_ACCESS_TOKEN");
        given(jwtTokenProvider.createRefreshToken(any())).willReturn("MOCK_REFRESH_TOKEN");
        given(passwordEncoder.matches(any(), any())).willReturn(true);

        //when
        AuthUserResponse result = userService.login(loginRequest);

        //then
        AuthUserResponse expected = AuthUserResponse.of(user, "MOCK_ACCESS_TOKEN", "MOCK_REFRESH_TOKEN");
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("토큰 재발급 성공")
    @Test
    void tokenReIssue() {
        //given
        ReissueRequest reissueRequest = new ReissueRequest(
                "nuts@naver.com",
                "MOCK_REFRESH_TOKEN"
        );

        given(userRepository.findByEmail(any())).willReturn(Optional.ofNullable(user));
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(redisService.getValues(any())).willReturn("MOCK_REFRESH_TOKEN");
        given(jwtTokenProvider.createAccessToken(any())).willReturn("MOCK_ACCESS_TOKEN");
        given(jwtTokenProvider.createRefreshToken(any())).willReturn("MOCK_REFRESH_TOKEN");

        //when
        TokenResponse result = userService.tokenReIssue(reissueRequest);

        //then
        TokenResponse expected = TokenResponse.of("MOCK_ACCESS_TOKEN", "MOCK_REFRESH_TOKEN");
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

}