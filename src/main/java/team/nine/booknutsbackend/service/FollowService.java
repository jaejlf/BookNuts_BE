package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.nine.booknutsbackend.domain.Follow;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.response.FollowResponse;
import team.nine.booknutsbackend.repository.FollowRepository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static team.nine.booknutsbackend.exception.ErrorMessage.*;


@RequiredArgsConstructor
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;

    //팔로우
    @Transactional
    public void follow(User following, User follower) {
        Follow follow = new Follow();

        if (followRepository.findByFollowingAndFollower(following, follower).isPresent())
            throw new EntityExistsException(ALREADY_FOLLOWING.getMsg());
        if (following == follower) throw new IllegalArgumentException(FOLLOW_ERROR.getMsg());

        follow.setFollowing(userService.findUserById(following.getUserId()));
        follow.setFollower(userService.findUserById(follower.getUserId()));

        followRepository.save(follow);
    }

    //언팔로우
    @Transactional
    public void unfollow(User unfollowing, User follower) {
        Follow follow = followRepository.findByFollowingAndFollower(unfollowing, follower)
                .orElseThrow(() -> new EntityNotFoundException(FOLLOW_NOT_FOUND.getMsg()));
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

    //회원 탈퇴 시, 모든 팔로우 관계 삭제
    @Transactional
    public void deleteAllFollow(User user) {
        followRepository.deleteAllByFollower(user);
    }

}
