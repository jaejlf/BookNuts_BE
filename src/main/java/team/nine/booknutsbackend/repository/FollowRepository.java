package team.nine.booknutsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.nine.booknutsbackend.domain.Follow;
import team.nine.booknutsbackend.domain.User;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Follow findByFollowingAndFollower(User following, User follower);
    boolean existsByFollowingAndFollower(User following, User follower);
    List<Follow> findByFollower(User curUser);
    List<Follow> findByFollowing(User curUser);
    void deleteAllByFollower(User user);
}