package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Set;


@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    @Query(nativeQuery = true, value = "select * from question where id = ?1 and status = ?2")
    Question findQuestionById(Integer id, String status);
    @Query(nativeQuery = true, value = "select * from question where id = ?1 and status = ?2")
    Question findQuestionToApproveById(Integer id, String status);
    @Query(nativeQuery = true, value = "SELECT * FROM question WHERE author_email = ?1 and status = ?2")
    Set<Question> findQuestionByAuthorEmail(String authorEmail, String status);
    @Query(nativeQuery = true, value = "SELECT * FROM question WHERE status = ?1")
    Set<Question> findAllQuestion(String status);
    @Query(nativeQuery = true, value = "SELECT * FROM question WHERE author_email = ?1 and status != ?2")
    Set<Question> findQuestionToDeleteByAuthorEmail(String authorEmail, String status);
    @Query(nativeQuery = true, value = "select * from question where id = ?1 and status != ?2")
    Question findQuestionToModifyById(Integer id, String status);

}
