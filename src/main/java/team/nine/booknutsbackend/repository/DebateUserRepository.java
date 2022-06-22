package team.nine.booknutsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.nine.booknutsbackend.domain.debate.DebateRoom;
import team.nine.booknutsbackend.domain.debate.DebateUser;
import team.nine.booknutsbackend.domain.User;

import java.util.Optional;

public interface DebateUserRepository extends JpaRepository<DebateUser, Long> {
    Optional<DebateUser> findByDebateRoomAndUser(DebateRoom room, User user);
    int countByDebateRoomAndOpinion(DebateRoom room, Boolean opinion);
}