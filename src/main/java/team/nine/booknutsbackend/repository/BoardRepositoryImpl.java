package team.nine.booknutsbackend.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.Comment;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.response.BoardResponse;

import java.util.List;
import java.util.Optional;

public class BoardRepositoryImpl implements BoardRepositoryCustom{

//    private final JPAQueryFactory queryFactory;
//
//    @Override
//    public Optional<Board> findByBoardIdAndUser(Long boardId, User user) {
//        Optional<BoardResponse> boardResponse = Optional.ofNullable(queryFactory
//                .selectFrom(Comment)
//                .leftJoin(Comment.parent)
//
//        return Optional.empty();
//    }

    @Override
    public Optional<Board> findByBoardIdAndUser(Long boardId, User user) {
        return Optional.empty();
    }

    @Override
    public List<Board> findByUser(User user) {
        return null;
    }
}
