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

    @Transactional
    public void follow(User me, User target) {
        checkFollowEnable(me, target);
        Follow follow = new Follow(
                userService.getUserById(me.getUserId()),
                userService.getUserById(target.getUserId())
        );
        followRepository.save(follow);
    }

    @Transactional
    public void unfollow(User me, User target) {
        Follow follow = getFollow(me, target);
        followRepository.delete(follow);
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

    private void checkFollowEnable(User me, User target) {
        if (followRepository.findByFollowingAndFollower(me, target).isPresent()) {
            throw new EntityExistsException(ALREADY_FOLLOWING.getMsg());
        }
        if (me == target) {
            throw new IllegalArgumentException(FOLLOW_ERROR.getMsg());
        }
    }

    private Follow getFollow(User me, User target) {
        return followRepository.findByFollowingAndFollower(me, target)
                .orElseThrow(() -> new EntityNotFoundException(FOLLOW_NOT_FOUND.getMsg()));
    }

}
