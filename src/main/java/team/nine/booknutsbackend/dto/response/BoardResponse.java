package team.nine.booknutsbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.User;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponse {

    Long boardId;
    String title;
    String content;
    String writer;
    LocalDateTime createdDate;
    String bookTitle;
    String bookAuthor;
    String bookImgUrl;
    String bookGenre;
    int nutsCnt;
    int heartCnt;
    int archiveCnt;
    Boolean isNuts;
    Boolean isHeart;
    Boolean isArchived;
    Boolean curUser;

    public static BoardResponse of(Board board, User writer) {
        return BoardResponse.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter().getNickname())
                .createdDate(board.getCreatedDate())
                .bookTitle(board.getBookTitle())
                .bookAuthor(board.getBookAuthor())
                .bookImgUrl(board.getBookImgUrl())
                .bookGenre(board.getBookGenre())
                .nutsCnt(board.getNutsList().size())
                .heartCnt(board.getHeartList().size())
                .archiveCnt(board.getArchiveBoards().size())
                .isNuts(board.isNuts(board, writer))
                .isHeart(board.isHeart(board, writer))
                .isArchived(board.isArchived(board, writer))
                .curUser(Objects.equals(board.getWriter().getUserId(), writer.getUserId()))
                .build();
    }

}