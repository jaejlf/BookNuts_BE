package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.domain.reaction.Heart;
import team.nine.booknutsbackend.domain.reaction.Nuts;
import team.nine.booknutsbackend.repository.HeartRepository;
import team.nine.booknutsbackend.repository.NutsRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReactionService {

    private final HeartRepository heartRepository;
    private final NutsRepository nutsRepository;
    private final BoardService boardService;

    @Transactional
    public String clickNuts(Long boardId, User user) {
        Board board = boardService.getBoard(boardId);
        List<Nuts> nutsList = user.getNutsList();
        Nuts targetNuts = nutsRepository.findByBoardAndUser(board, user);

        if (nutsList.contains(targetNuts)) {
            nutsRepository.delete(targetNuts);
            return "넛츠 취소";
        }

        Nuts nuts = new Nuts(board, user);
        nutsRepository.save(nuts);
        return "넛츠 누름";
    }

    @Transactional
    public String clickHeart(Long boardId, User user) {
        Board board = boardService.getBoard(boardId);
        List<Heart> heartList = user.getHearts();
        Heart targetHeart = heartRepository.findByBoardAndUser(board, user);

        if (heartList.contains(targetHeart)) {
            heartRepository.delete(targetHeart);
            return "좋아요 취소";
        }

        Heart heart = new Heart(board, user);
        heartRepository.save(heart);
        return "좋아요 누름";
    }

    @Transactional
    public void deleteAllReaction(User user) {
        nutsRepository.deleteAllByUser(user);
        heartRepository.deleteAllByUser(user);
    }

}
