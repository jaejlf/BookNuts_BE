package team.nine.booknutsbackend.dto.response;

import lombok.Builder;
import lombok.Getter;
import team.nine.booknutsbackend.domain.User;

@Getter
@Builder
public class FollowResponse {

    Long userId;
    String loginId;
    String username;
    String nickname;
    String email;

    public static FollowResponse followUserResponse(User user) {
        return FollowResponse.builder()
                .userId(user.getUserId())
                .loginId(user.getLoginId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .build();
    }

}
