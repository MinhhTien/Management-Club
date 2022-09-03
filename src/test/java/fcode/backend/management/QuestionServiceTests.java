package fcode.backend.management;

import fcode.backend.management.model.dto.QuestionDTO;
import fcode.backend.management.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@SpringBootTest
class QuestionServiceTests {
    @Autowired
    QuestionService questionService;
    @Test
    void addQuestion() {
        questionService.createQuestion(new QuestionDTO("title1", "content1", "author1@gmail.com"));
        questionService.createQuestion(new QuestionDTO("title2", "content2", "author2@gmail.com"));
        questionService.createQuestion(new QuestionDTO("title3", "content3", "author1@gmail.com"));
        questionService.createQuestion(new QuestionDTO("title4", "content4", "author1@gmail.com"));

    }

    @Test
    void approveQuestion() {
        questionService.approveQuestion(1);
        questionService.approveQuestion(5);
        questionService.disapproveQuestion(4);
        questionService.approveQuestion(3);
    }


    @Test
    void getQuestions() {
        Set<QuestionDTO> questionDTOSet;
        QuestionDTO questionDTO;
        questionDTOSet = questionService.getAllQuestions().getData();
        questionDTOSet.forEach(System.out::println);
        questionDTO = questionService.getQuestionById(7).getData();
        System.out.println(questionDTO);
        questionDTO = questionService.getQuestionById(2).getData();
        System.out.println(questionDTO);
        questionDTO = questionService.getQuestionById(4).getData();
        System.out.println(questionDTO);
        questionDTO = questionService.getQuestionById(1).getData();
        System.out.println(questionDTO);
        questionDTOSet = questionService.getQuestionByAuthor("author3@gmail.com").getData();
        if (questionDTOSet != null) questionDTOSet.forEach(System.out::println);
        questionDTOSet = questionService.getQuestionByAuthor("author1@gmail.com").getData();
        if (questionDTOSet != null) questionDTOSet.forEach(System.out::println);
        questionDTOSet = questionService.getQuestionByAuthor("author2@gmail.com").getData();
        if (questionDTOSet != null) questionDTOSet.forEach(System.out::println);
    }

    @Test
    void updateQuestion1() {
        questionService.updateQuestion(new QuestionDTO(3), null, "new content 3");

        questionService.updateQuestion(new QuestionDTO(2), null, "new content 2");
        questionService.updateQuestion(new QuestionDTO(2), "new title 2", null);
        questionService.updateTitle(new QuestionDTO(1), null);
        questionService.updateContent(new QuestionDTO(1), null);

    }

    @Test
    void updateQuestion2() {
        questionService.updateQuestion(new QuestionDTO(3), "new title 3", null);
        questionService.updateTitle(new QuestionDTO(1), "new title 1");
        questionService.updateContent(new QuestionDTO(1), "new content 1");
    }

    @Test
    void deleteQuestion() {
        questionService.deleteQuestion(2);
        questionService.deleteQuestionByAuthorEmail("author1@gmail.com");
    }
    @Test
    void restoreQuestion() {
        questionService.restoreQuestionByAuthorEmail("author1@gmail.com");
    }
}
