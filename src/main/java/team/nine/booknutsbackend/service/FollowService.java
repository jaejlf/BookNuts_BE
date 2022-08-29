package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.nine.booknutsbackend.domain.Follow;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.response.FollowResponse;
import team.nine.booknutsbackend.repository.FollowRepository;

import java.util.List;
import java.util.stream.Collectors;


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
        return followingToDto(followList);
    }

    @Transactional(readOnly = true)
    public List<FollowResponse> getMyFollowerList(User user) {
        List<Follow> followList = followRepository.findByFollowing(user);
        return followerToDto(followList);
    }

    @Transactional
    public void deleteAllFollow(User user) {
        followRepository.deleteAllByFollower(user);
    }

    private List<FollowResponse> followingToDto(List<Follow> followList) {
        return followList.stream().map(x -> FollowResponse.of(x.getFollowing())).collect(Collectors.toList());
    }

    private List<FollowResponse> followerToDto(List<Follow> followList) {
        return followList.stream().map(x -> FollowResponse.of(x.getFollower())).collect(Collectors.toList());
    }

}