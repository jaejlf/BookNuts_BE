package team.nine.booknutsbackend.service;

import org.springframework.data.jpa.domain.Specification;
import team.nine.booknutsbackend.domain.debate.DebateRoom;

public class SearchSpecs {

    /*
    토론방
    */
    public static Specification<DebateRoom> likeTopic(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("topic"), "%" + keyword + "%");
    }

    /*
    책
    */
    public static <T> Specification<T> likeBookTitle(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("bookTitle"), "%" + keyword + "%");
    }

    public static <T> Specification<T> likeBookAuthor(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("bookAuthor"), "%" + keyword + "%");
    }

}
