package team.nine.booknutsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import team.nine.booknutsbackend.domain.debate.DebateRoom;
import team.nine.booknutsbackend.enumerate.DebateStatus;
import team.nine.booknutsbackend.enumerate.DebateType;

import java.util.List;

public interface DebateRoomRepository extends JpaRepository<DebateRoom, Long>, JpaSpecificationExecutor<DebateRoom> {
    List<DebateRoom> findByTypeAndStatus(DebateType type, DebateStatus status);
    List<DebateRoom> findByStatus(DebateStatus status);
}