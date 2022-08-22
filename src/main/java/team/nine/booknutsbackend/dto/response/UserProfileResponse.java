package team.nine.booknutsbackend.dto.response;

import lombok.Builder;
import lombok.Getter;
import team.nine.booknutsbackend.domain.Follow;
import team.nine.booknutsbackend.domain.User;

import java.util.List;

@Getter
@Builder
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
                .isFollow(getIsFollow(me, target))
                .followerCount(target.getFollowingList().size())
                .followingCount(target.getFollowerList().size())
                .build();
    }

    private static Boolean getIsFollow(User me, User target) {
        List<Follow> followList = me.getFollowerList();
        for (Follow follow : followList) {
            if (follow.getFollower() == target) return true;
        }
        return false;
    }

}