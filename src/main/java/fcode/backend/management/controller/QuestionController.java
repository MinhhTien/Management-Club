package fcode.backend.management.controller;

import fcode.backend.management.model.dto.QuestionDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    QuestionService questionService;
    @PostMapping(value = "/postQuestion")
    public Response<Void> createQuestion(@RequestBody QuestionDTO questionDTO) {
        return questionService.createQuestion(questionDTO);
    }
    @PutMapping
    public Response<Void> updateQuestion(@RequestBody QuestionDTO questionDTO, String title, String content) {
        return questionService.updateQuestion(questionDTO, title, content);
    }
    @PutMapping("/approve/{questionId}")
    public Response<Void> approveQuestion(@PathVariable Integer questionId) {
        return questionService.approveQuestion(questionId);
    }
    @PutMapping("/disapprove/{questionId}")
    public Response<Void> disapproveQuestion(@PathVariable Integer questionId) {
        return questionService.disapproveQuestion(questionId);
    }
    @DeleteMapping(value = "/{questionId}")
    public Response<Void> deleteQuestion(@PathVariable Integer questionId) {
        return questionService.deleteQuestion(questionId);
    }
    @DeleteMapping(value = "/deleteByAuthor")
    public Response<Void> deleteQuestionByAuthor(@RequestAttribute String authorEmail) {
        return questionService.deleteQuestionByAuthorEmail(authorEmail);
    }
    @GetMapping(value = "/{questionId}")
    public Response<QuestionDTO> getQuestionById(@PathVariable Integer questionId) {
        return questionService.getQuestionById(questionId);
    }
    @GetMapping(value = "/allQuestions")
    public Response<Set<QuestionDTO>> getAllQuestions() {
        return questionService.getAllQuestions();
    }
    @GetMapping(value = "/getByAuthor")
    public Response<Set<QuestionDTO>> getQuestionsByAuthor(@RequestAttribute String authorEmail) {
        return questionService.getQuestionByAuthor(authorEmail);
    }


}
