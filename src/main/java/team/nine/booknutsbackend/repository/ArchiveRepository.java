package team.nine.booknutsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.nine.booknutsbackend.domain.archive.Archive;
import team.nine.booknutsbackend.repository.custom.CustomArchiveRepository;

public interface ArchiveRepository extends JpaRepository<Archive, Long>, CustomArchiveRepository {
}