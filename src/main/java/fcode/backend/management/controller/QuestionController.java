package fcode.backend.management.controller;

import fcode.backend.management.model.dto.QuestionDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    QuestionService questionService;
    @PostMapping
    public Response<Void> createQuestion(@RequestBody QuestionDTO questionDTO, @RequestAttribute(required = false) String userEmail) {
        return questionService.createQuestion(questionDTO, userEmail);
    }
    @PutMapping
    public Response<Void> updateQuestion(@RequestBody QuestionDTO questionDTO, @RequestAttribute(required = false) String userEmail) {
        return questionService.updateQuestion(questionDTO, userEmail);
    }
    @PutMapping("/approve/{questionId}")
    public Response<Void> approveQuestion(@PathVariable Integer questionId) {
        return questionService.approveQuestion(questionId);
    }

    @PutMapping("/approve/all")
    public Response<Void> approveAllQuestions() {
        return questionService.approveAll();
    }
    @PutMapping("/disapprove/{questionId}")
    public Response<Void> disapproveQuestion(@PathVariable Integer questionId) {
        return questionService.disapproveQuestion(questionId);
    }

    @PutMapping("/disapprove/all")
    public Response<Void> disapproveAllQuestions() {
        return questionService.disapproveAll();
    }
    @DeleteMapping(value = "/{questionId}")
    public Response<Void> deleteQuestion(@PathVariable Integer questionId, @RequestAttribute(required = false) String userEmail) {
        return questionService.deleteQuestion(questionId, userEmail);
    }

    @GetMapping(value = "/{questionId}")
    public Response<QuestionDTO> getQuestionById(@PathVariable Integer questionId) {
        return questionService.getQuestionById(questionId);
    }
    @GetMapping(value = "/questions")
    public Response<List<QuestionDTO>> getAllQuestions() {
        return questionService.getAllQuestions();
    }
    @GetMapping(value = "/author")
    public Response<List<QuestionDTO>> getQuestionsByAuthor(@RequestAttribute(required = false) String userEmail) {
        return questionService.getQuestionByAuthor(userEmail);
    }

    @GetMapping("/processing")
    public Response<List<QuestionDTO>> getProcessingQuestions() {
        return questionService.getProcessingQuestions();
    }

    @GetMapping("/inactive")
    public Response<List<QuestionDTO>> getInactiveQuestions() {
        return questionService.getInactiveQuestions();
    }

}
