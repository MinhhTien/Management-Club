package fcode.backend.management;

import fcode.backend.management.model.dto.CommentDTO;
import fcode.backend.management.model.dto.QuestionDTO;
import fcode.backend.management.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CommentServiceTests {
    @Autowired
    CommentService commentService;
    @Test
    void addComment() {
        commentService.createComment(new CommentDTO("content comment 1", "authorcomment1@gmail.com", 1));
        commentService.createComment(new CommentDTO("content comment 2", "authorcomment1@gmail.com", 1));
        commentService.createComment(new CommentDTO("content comment 3", "author1@gmail.com", 2));
        commentService.createComment(new CommentDTO("content comment 4", "author2@gmail.com", 3));
    }

    @Test
    void getComment() {
        System.out.println(commentService.getCommentById(1).getData());
        commentService.getAllCommentsOfAQuestion(1).getData().forEach(System.out::println);
    }

    @Test
    void updateComment() {
        commentService.updateContent(new CommentDTO(2), "new comment content");
        commentService.updateContent(new CommentDTO(1), "new comment content 2");
        commentService.updateContent(new CommentDTO(1), null);
        commentService.updateContent(new CommentDTO(3), "new content 3");
    }
    @Test
    void deleteComment() {
        commentService.deleteComment(1);
        commentService.deleteAllCommentByAuthorEmail("authorcomment1@gmail.com");
    }
}
