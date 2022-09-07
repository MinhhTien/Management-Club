package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Question;
import fcode.backend.management.service.constant.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Set;


@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    Question findQuestionByIdAndStatus(Integer id, Status status);

    Set<Question> findQuestionByAuthorEmailAndStatus(String authorEmail, Status status);

    Set<Question> findQuestionByStatus(Status status);

    Set<Question> findQuestionByAuthorEmailAndStatusIsNot(String authorEmail, Status status);

    Question findQuestionByIdAndStatusIsNot(Integer id, Status status);

}
