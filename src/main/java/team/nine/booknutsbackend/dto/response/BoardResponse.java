package team.nine.booknutsbackend.dto.response;

import lombok.Builder;
import lombok.Getter;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.domain.archive.ArchiveBoard;
import team.nine.booknutsbackend.domain.reaction.Heart;
import team.nine.booknutsbackend.domain.reaction.Nuts;

import java.util.List;
import java.util.Objects;

@Getter
@Builder
public class BoardResponse {

    Long boardId;
    String title;
    String content;
    String writer;
    String createdDate;
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
                .isNuts(getIsNuts(board, writer))
                .isHeart(getIsHeart(board, writer))
                .isArchived(getIsArchived(board, writer))
                .curUser(Objects.equals(board.getWriter().getUserId(), writer.getUserId()))
                .build();
    }

    private static Boolean getIsNuts(Board board, User writer) {
        List<Nuts> nutsList = writer.getNutsList();
        for (Nuts nuts : nutsList) {
            if (nuts.getBoard().equals(board)) return true;
        }
        return false;
    }

    private static Boolean getIsHeart(Board board, User writer) {
        List<Heart> hearts = writer.getHearts();
        for (Heart heart : hearts) {
            if (heart.getBoard().equals(board)) return true;
        }
        return false;
    }

    private static Boolean getIsArchived(Board board, User writer) {
        List<ArchiveBoard> archiveBoards = writer.getArchiveBoards();
        for (ArchiveBoard archiveBoard : archiveBoards) {
            if (archiveBoard.getBoard().equals(board)) return true;
        }
        return false;
    }

}