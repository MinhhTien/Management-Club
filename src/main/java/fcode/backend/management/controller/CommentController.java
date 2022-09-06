package fcode.backend.management.controller;

import fcode.backend.management.model.dto.CommentDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequestMapping("/comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    @PostMapping
    public Response<Void> createComment(@RequestBody CommentDTO commentDTO) {
        return commentService.createComment(commentDTO);
    }

    @GetMapping("/{commentId}")
    public Response<CommentDTO> getCommentById(@PathVariable Integer commentId) {
        return commentService.getCommentById(commentId);
    }
    @GetMapping("/getByQuestion/{questionId}")
    public Response <Set<CommentDTO>> getAllCommentsOfAQuestion(@PathVariable Integer questionId) {
        return commentService.getAllCommentsOfAQuestion(questionId);
    }
    @PutMapping
    public Response<Void> updateComment(@RequestBody CommentDTO commentDTO ,@RequestAttribute String content) {
        return commentService.updateContent(commentDTO, content);
    }

    @DeleteMapping("/{commentId}")
    public Response<Void> deleteCommentById(@PathVariable Integer commentId) {
        return commentService.deleteComment(commentId);
    }

    @DeleteMapping("/deleteByAuthor")
    public Response<Void> deleteCommentByAuthor(@RequestAttribute String authorEmail) {
        return commentService.deleteAllCommentByAuthorEmail(authorEmail);
    }
}
