package team.nine.booknutsbackend.repository;

import lombok.RequiredArgsConstructor;
import team.nine.booknutsbackend.domain.Comment;

import java.util.List;

@RequiredArgsConstructor
public class CommentRepositoryImpl{
//    private final JPAQueryFactory queryFactory;
//
//    @Override
//    public List<Comment> findCommentByBoardId(Long boardId) {
//        return queryFactory.selectFrom(comment)
//                .leftJoin(comment.parent)
//                .fetchJoin()
//                .where(comment.board.boardId.eq(boardId))
//                .orderBy(
//                        comment.parent.commentId.asc().nullsFirst(),
//                        comment.createdDate.asc()
//                ).fetch();
//    }
}
