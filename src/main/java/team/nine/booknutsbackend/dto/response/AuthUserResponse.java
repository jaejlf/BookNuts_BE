package team.nine.booknutsbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.nine.booknutsbackend.domain.User;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserResponse {

    Long userId;
    String loginId;
    String username;
    String nickname;
    String email;
    String accessToken;
    String refreshToken;
    String profileImgUrl;

    public static AuthUserResponse of(User user, String accessToken, String refreshToken) {
        return AuthUserResponse.builder()
                .userId(user.getUserId())
                .loginId(user.getLoginId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .profileImgUrl(user.getProfileImgUrl())
                .build();
    }
}