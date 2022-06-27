package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.nine.booknutsbackend.domain.Follow;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.response.FollowResponse;
import team.nine.booknutsbackend.exception.follow.FollowDuplicateException;
import team.nine.booknutsbackend.repository.FollowRepository;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final AuthService userService;

    //팔로우
    @Transactional
    public void follow(User following, User follower) {
        Follow follow = new Follow();

        if (followRepository.findByFollowingAndFollower(following, follower).isPresent())
            throw new FollowDuplicateException("이미 팔로잉한 계정입니다");

        follow.setFollowing(userService.findUserById(following.getUserId()));
        follow.setFollower(userService.findUserById(follower.getUserId()));

        followRepository.save(follow);
    }

    //언팔로우
    @Transactional
    public void unfollow(User unfollowing, User follower) {
        Follow follow = followRepository.findByFollowingAndFollower(unfollowing, follower)
                .orElseThrow(() -> new FollowDuplicateException("팔로우하지 않은 계정입니다."));
        followRepository.delete(follow);
    }

    //나의 팔로잉 리스트
    @Transactional(readOnly = true)
    public List<FollowResponse> getMyFollowingList(User user) {
        List<Follow> followList = followRepository.findByFollower(user);
        List<FollowResponse> followingList = new ArrayList<>();

        for (Follow follow : followList) {
            followingList.add(FollowResponse.followUserResponse(follow.getFollowing()));
        }

        return followingList;
    }

    //나의 팔로워 리스트
    @Transactional(readOnly = true)
    public List<FollowResponse> getMyFollowerList(User user) {
        List<Follow> followList = followRepository.findByFollowing(user);
        List<FollowResponse> followerList = new ArrayList<>();

        for (Follow follow : followList) {
            followerList.add(FollowResponse.followUserResponse(follow.getFollower()));
        }

        return followerList;

    }

}
