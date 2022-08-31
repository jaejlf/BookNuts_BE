package team.nine.booknutsbackend.repository.custom;

import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.domain.archive.Archive;

import java.util.List;

public interface CustomArchiveRepository {
    List<Archive> findAllByOwner(User user);
}
