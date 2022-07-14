package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.domain.reaction.Heart;
import team.nine.booknutsbackend.domain.reaction.Nuts;
import team.nine.booknutsbackend.exception.board.BoardNotFoundException;
import team.nine.booknutsbackend.repository.BoardRepository;
import team.nine.booknutsbackend.repository.HeartRepository;
import team.nine.booknutsbackend.repository.NutsRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReactionService {

    private final HeartRepository heartRepository;
    private final NutsRepository nutsRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public String clickNuts(Long boardId, User user) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);

        List<Nuts> nutsList = user.getNutsList();
        Nuts targetNuts = nutsRepository.findByBoardAndUser(board, user);

        if (nutsList.contains(targetNuts)) {
            nutsRepository.delete(targetNuts);
            return "넛츠 취소";
        }

        Nuts nuts = new Nuts();
        nuts.setBoard(board);
        nuts.setUser(user);
        nutsRepository.save(nuts);
        return "넛츠 누름";
    }

    @Transactional
    public String clickHeart(Long boardId, User user) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);

        List<Heart> hearts = user.getHearts();
        Heart targetHeart = heartRepository.findByBoardAndUser(board, user);

        if (hearts.contains(targetHeart)) {
            heartRepository.delete(targetHeart);
            return "좋아요 취소";
        }

        Heart heart = new Heart();
        heart.setBoard(board);
        heart.setUser(user);
        heartRepository.save(heart);
        return "좋아요 누름";
    }

    //회원 탈퇴 시, 모든 넛츠/좋아요 삭제
    @Transactional
    public void deleteAllReaction(User user) {
        nutsRepository.deleteAllByUser(user);
        heartRepository.deleteAllByUser(user);
    }

}
