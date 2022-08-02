package team.nine.booknutsbackend.dto.response;

import lombok.Builder;
import lombok.Getter;
import team.nine.booknutsbackend.domain.User;

@Getter
@Builder
public class LoginResponse {

    Long userId;
    String loginId;
    String username;
    String nickname;
    String email;
    String accessToken;
    String refreshToken;
    String profileImgUrl;

    public static LoginResponse loginResponse(User user, String accessToken) {
        return LoginResponse.builder()
                .userId(user.getUserId())
                .loginId(user.getLoginId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .accessToken(accessToken)
                .refreshToken(user.getRefreshToken())
                .profileImgUrl(user.getProfileImgUrl())
                .build();
    }

}
