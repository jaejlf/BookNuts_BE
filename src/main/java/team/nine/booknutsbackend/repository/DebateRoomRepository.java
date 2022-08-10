package team.nine.booknutsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import team.nine.booknutsbackend.domain.debate.DebateRoom;

import java.util.List;

public interface DebateRoomRepository extends JpaRepository<DebateRoom, Long>, JpaSpecificationExecutor<DebateRoom> {
    List<DebateRoom> findByTypeAndStatus(int type, int status);
    List<DebateRoom> findByStatus(int status);
}