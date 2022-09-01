package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Set;


@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    @Query(nativeQuery = true, value = "select * from question where status = \"Active\" and id = ?1")
    Question findQuestionById(Integer id);
    @Query(nativeQuery = true, value = "SELECT * FROM question WHERE status = \"Active\" and author_email = ?1")
    Set<Question> findQuestionByAuthorEmail(String authorEmail);
    @Query(nativeQuery = true, value = "SELECT * FROM question WHERE status = \"Active\"")
    Set<Question> findAllQuestion();
}
