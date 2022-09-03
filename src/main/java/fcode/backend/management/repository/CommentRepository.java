package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM comment WHERE id = ?1 AND status = \"Active\"")
    Comment findCommentById(Integer id);
    @Query(nativeQuery = true, value = "SELECT * FROM comment WHERE id = ?1 AND status != \"Inactive\"")
    Comment findCommentToModifyById(Integer id);
    @Query(nativeQuery = true, value = "SELECT * FROM comment WHERE status != \"Inactive\" and author_email = ?1")
    Set<Comment> findCommentToDeleteByAuthorEmail(String authorEmail);
}
