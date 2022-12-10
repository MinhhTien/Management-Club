package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Question;
import fcode.backend.management.service.constant.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    Question findQuestionByIdAndStatus(Integer id, Status status);

    List<Question> findQuestionByAuthorEmailAndStatus(String authorEmail, Status status);

    List<Question> findQuestionByStatus(Status status);

    List<Question> findQuestionByAuthorEmailAndStatusIsNot(String authorEmail, Status status);

    Question findQuestionByIdAndStatusIsNot(Integer id, Status status);

}
