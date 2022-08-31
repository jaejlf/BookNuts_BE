package team.nine.booknutsbackend.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import team.nine.booknutsbackend.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByLoginId(String loginId);
    Optional<User> findByNickname(String nickname);
    boolean existsByNickname(String nickname);
    boolean existsByLoginId(String loginId);
    List<User> findAllByEnabledAndRequestedDeleteAtBetween(boolean enabled, LocalDateTime startTime, LocalDateTime endTime);
    List<User> findAllByNicknameContaining(@Param("keyword") String nickname);
}