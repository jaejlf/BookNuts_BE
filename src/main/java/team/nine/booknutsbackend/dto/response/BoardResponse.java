package team.nine.booknutsbackend.dto.response;

import lombok.Builder;
import lombok.Getter;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.domain.archive.ArchiveBoard;
import team.nine.booknutsbackend.domain.reaction.Heart;
import team.nine.booknutsbackend.domain.reaction.Nuts;

import java.util.ArrayList;
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
    //int commentCnt;
    Boolean isNuts;
    Boolean isHeart;
    Boolean isArchived;
    Boolean curUser;
    List<CommentResponse> comments = new ArrayList<>();

    public static BoardResponse boardResponse(Board board, User user) {
        return BoardResponse.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(getNickname(board))
                .createdDate(board.getCreatedDate())
                .bookTitle(board.getBookTitle())
                .bookAuthor(board.getBookAuthor())
                .bookImgUrl(board.getBookImgUrl())
                .bookGenre(board.getBookGenre())
                .nutsCnt(board.getNutsList().size())
                .heartCnt(board.getHeartList().size())
                //.commentCnt(board.getComments().size())
                .archiveCnt(board.getArchiveBoards().size())
                .isNuts(getIsNuts(board, user))
                .isHeart(getIsHeart(board, user))
                .isArchived(getIsArchived(board, user))
                .curUser(Objects.equals(board.getUser().getUserId(), user.getUserId()))
                .build();
    }

    private static String getNickname(Board board) {
        if(!board.getUser().isEnabled()) return "(탈퇴한 회원)";
        else return board.getUser().getNickname();
    }

    private static Boolean getIsNuts(Board board, User user) {
        List<Nuts> nutsList = user.getNutsList();
        for (Nuts nuts : nutsList) {
            if (nuts.getBoard().equals(board)) return true;
        }
        return false;
    }

    private static Boolean getIsHeart(Board board, User user) {
        List<Heart> hearts = user.getHearts();
        for (Heart heart : hearts) {
            if (heart.getBoard().equals(board)) return true;
        }
        return false;
    }

    private static Boolean getIsArchived(Board board, User user) {
        List<ArchiveBoard> archiveBoards = user.getArchiveBoards();
        for (ArchiveBoard archiveBoard : archiveBoards) {
            if (archiveBoard.getBoard().equals(board)) return true;
        }
        return false;
    }

}
