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

@RequiredArgsConstructor
@Service
public class ReactionService {

    private final HeartRepository heartRepository;
    private final NutsRepository nutsRepository;
    private final BoardService boardService;

    @Transactional
    public String clickNuts(Long boardId, User user) {
        Board board = boardService.getBoard(boardId);
        if (nutsRepository.existsByBoardAndUser(board, user)) {
            nutsRepository.delete(nutsRepository.findByBoardAndUser(board, user));
            return "넛츠 취소";
        } else {
            Nuts nuts = new Nuts(board, user);
            nutsRepository.save(nuts);
            return "넛츠 누름";
        }
    }

    @Transactional
    public String clickHeart(Long boardId, User user) {
        Board board = boardService.getBoard(boardId);
        if (heartRepository.existsByBoardAndUser(board, user)) {
            heartRepository.delete(heartRepository.findByBoardAndUser(board, user));
            return "좋아요 취소";
        } else {
            Heart heart = new Heart(board, user);
            heartRepository.save(heart);
            return "좋아요 누름";
        }
    }

    @Transactional
    public void deleteAllReaction(User user) {
        nutsRepository.deleteAllByUser(user);
        heartRepository.deleteAllByUser(user);
    }

}
