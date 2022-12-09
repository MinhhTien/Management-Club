package fcode.backend.management.controller;

import fcode.backend.management.model.dto.CommentDTO;
import fcode.backend.management.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CommentControllerTest {
    @Autowired
    CommentService commentService;

    @Test
    void getCommentById() {
        List<CommentDTO> commentDTOList = commentService.getLatestComments(3).getData();
        commentDTOList.forEach(System.out::println);

    }

    @Test
    void getLatestCommentsOfAQuestion() {
    }
}