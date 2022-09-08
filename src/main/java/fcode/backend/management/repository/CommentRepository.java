package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Comment;
import fcode.backend.management.service.constant.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Comment findCommentByIdAndStatus(Integer id, Status status);
    Comment findCommentByIdAndStatusIsNot(Integer id, Status status);
    Set<Comment> findCommentByAuthorEmailAndStatusIsNot(String authorEmail, Status status);
}
