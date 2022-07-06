package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.nine.booknutsbackend.domain.Comment;
import team.nine.booknutsbackend.dto.response.CommentResponse;
import team.nine.booknutsbackend.repository.BoardRepository;
import team.nine.booknutsbackend.repository.CommentRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentService {

    private BoardRepository boardRepository;
    private CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public List<CommentResponse> findBoardComment(Long boardId) {
        //존재하는 게시글인지 확인
        //boardRepository.findById()
        return convertNestedStructure(commentRepository.findCommentByBoardWithParentComment(boardId));
    }

    private List<CommentResponse> convertNestedStructure(List<Comment> comments) {
        List<CommentResponse> resultcomment = new ArrayList<>();
        Map<Long, CommentResponse> commentResponseMap = new HashMap<>();
        comments.stream().forEach(c -> {
            CommentResponse response = CommentResponse.converCommentToResponse(c);
            commentResponseMap.put(response.getCommentId(), response);
        });
        return  resultcomment;
    }
}
