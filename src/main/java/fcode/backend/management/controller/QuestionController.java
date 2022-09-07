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
    @PostMapping()
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
    @DeleteMapping(value = "/author")
    public Response<Void> deleteQuestionByAuthor(@RequestAttribute String userEmail) {
        return questionService.deleteQuestionByAuthorEmail(userEmail);
    }
    @GetMapping(value = "/{questionId}")
    public Response<QuestionDTO> getQuestionById(@PathVariable Integer questionId) {
        return questionService.getQuestionById(questionId);
    }
    @GetMapping(value = "/questions")
    public Response<Set<QuestionDTO>> getAllQuestions() {
        return questionService.getAllQuestions();
    }
    @GetMapping(value = "/author")
    public Response<Set<QuestionDTO>> getQuestionsByAuthor(@RequestAttribute String userEmail) {
        return questionService.getQuestionByAuthor(userEmail);
    }


}
