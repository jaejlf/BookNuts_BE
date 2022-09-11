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
public class FollowResponse {

    Long userId;
    String loginId;
    String username;
    String nickname;
    String email;

    public static FollowResponse of(User user) {
        return FollowResponse.builder()
                .userId(user.getUserId())
                .loginId(user.getLoginId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .build();
    }

}