package team.nine.booknutsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.response.ResultResponse;
import team.nine.booknutsbackend.service.ReactionService;

import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reaction")
public class ReactionController {

    private final ReactionService reactionService;

    @PutMapping("/nuts/{boardId}")
    public ResponseEntity<Object> clickNuts(@PathVariable Long boardId,
                                            @AuthenticationPrincipal User user) {
        String result = reactionService.clickNuts(boardId, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(boardId + "번 게시글 " + result));
    }

    @PutMapping("/heart/{boardId}")
    public ResponseEntity<Object> clickHeart(@PathVariable Long boardId,
                                             @AuthenticationPrincipal User user) {
        String result = reactionService.clickHeart(boardId, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(boardId + "번 게시글 " + result));
    }

}