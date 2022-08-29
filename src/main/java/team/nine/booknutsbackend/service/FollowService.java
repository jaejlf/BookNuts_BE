package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.nine.booknutsbackend.domain.Follow;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.response.FollowResponse;
import team.nine.booknutsbackend.repository.FollowRepository;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;

    @Transactional
    public String clickFollow(User me, User target) {
        if (followRepository.existsByFollowingAndFollower(me, target)) {
            followRepository.delete(followRepository.findByFollowingAndFollower(me, target));
            return "언팔로우";
        } else {
            Follow follow = new Follow(
                    userService.getUserByNickname(me.getNickname()),
                    userService.getUserByNickname(target.getNickname())
            );
            followRepository.save(follow);
            return "팔로우";
        }
    }

    @Transactional(readOnly = true)
    public List<FollowResponse> getFollowingList(User user) {
        List<Follow> followList = followRepository.findByFollower(user);
        List<FollowResponse> followResponseList = new ArrayList<>();
        for (Follow follow : followList) {
            followResponseList.add(FollowResponse.of(follow.getFollowing()));
        }
        return followResponseList;
    }

    @Transactional(readOnly = true)
    public List<FollowResponse> getMyFollowerList(User user) {
        List<Follow> followList = followRepository.findByFollowing(user);
        List<FollowResponse> followResponseList = new ArrayList<>();
        for (Follow follow : followList) {
            followResponseList.add(FollowResponse.of(follow.getFollower()));
        }
        return followResponseList;
    }

    @Transactional
    public void deleteAllFollow(User user) {
        followRepository.deleteAllByFollower(user);
    }

}