package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Question findOneById(Integer id);
    Set<Question> findQuestionByAuthorEmail(String authorEmail);
}
