package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM comment WHERE id = ?1 AND status = ?2")
    Comment findCommentById(Integer id, String status);
    @Query(nativeQuery = true, value = "SELECT * FROM comment WHERE id = ?1 AND status != ?2")
    Comment findCommentToModifyById(Integer id, String status);
    @Query(nativeQuery = true, value = "SELECT * FROM comment WHERE author_email = ?1 and status != ?2")
    Set<Comment> findCommentToDeleteByAuthorEmail(String authorEmail, String status);
}
