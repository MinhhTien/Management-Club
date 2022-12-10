package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Comment;
import fcode.backend.management.repository.entity.Question;
import fcode.backend.management.service.constant.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Comment findCommentByIdAndStatus(Integer id, Status status);
    Comment findCommentByIdAndStatusIsNot(Integer id, Status status);
    List<Comment> findCommentByAuthorEmailAndStatusIsNot(String authorEmail, Status status);
    List<Comment> findTop10ByQuestionAndStatusOrderByCreatedTimeDesc(Question question, Status status);
}
