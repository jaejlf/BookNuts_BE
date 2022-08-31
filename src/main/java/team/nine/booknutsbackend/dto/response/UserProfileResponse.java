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
public class UserProfileResponse {

    Long userId;
    String nickname;
    Boolean isMyProfile;
    Boolean isFollow;
    int followerCount;
    int followingCount;

    public static UserProfileResponse of(User me, User target) {
        return UserProfileResponse.builder()
                .userId(target.getUserId())
                .nickname(target.getNickname())
                .isMyProfile(me == target)
                .isFollow(target.isFollow(me, target))
                .followerCount(target.getFollowingList().size())
                .followingCount(target.getFollowerList().size())
                .build();
    }

}