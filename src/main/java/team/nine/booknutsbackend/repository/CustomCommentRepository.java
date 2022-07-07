package team.nine.booknutsbackend.repository;

import team.nine.booknutsbackend.domain.Comment;

import java.util.List;

public interface CustomCommentRepository {
    List<Comment> findCommentByBoardId(Long boardId);
}
