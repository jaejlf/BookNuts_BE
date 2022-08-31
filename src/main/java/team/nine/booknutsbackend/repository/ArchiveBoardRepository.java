package team.nine.booknutsbackend.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.domain.archive.Archive;
import team.nine.booknutsbackend.domain.archive.ArchiveBoard;
import team.nine.booknutsbackend.repository.custom.CustomArchiveBoardRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ArchiveBoardRepository extends JpaRepository<ArchiveBoard, User>, CustomArchiveBoardRepository {
    List<ArchiveBoard> findByArchive(Archive archive);
    ArchiveBoard findByArchiveAndBoard(Archive archive, Board board);
    Optional<ArchiveBoard> findByBoardAndOwner(Board board, User owner);
    boolean existsByBoardAndOwner(Board board, User user);

    @Transactional
    @Modifying
    @Query("delete from ArchiveBoard a where a.archive.archiveId = :archiveId")
    void deleteAllByArchiveId(@Param("archiveId") Long archiveId);
}