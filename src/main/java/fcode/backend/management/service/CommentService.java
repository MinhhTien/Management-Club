package fcode.backend.management.service;

import fcode.backend.management.model.dto.CommentDTO;
import fcode.backend.management.model.dto.QuestionDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.CommentRepository;
import fcode.backend.management.repository.QuestionRepository;
import fcode.backend.management.repository.entity.Comment;
import fcode.backend.management.repository.entity.Question;
import fcode.backend.management.service.constant.ServiceMessage;
import fcode.backend.management.service.constant.ServiceStatusCode;
import fcode.backend.management.service.constant.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
@Service
public class CommentService {


    @Autowired
    CommentRepository commentRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    ModelMapper modelMapper;
    private static final Logger logger = LogManager.getLogger(CommentService.class);
    private static final String CREATE_COMMENT_MESSAGE = "Create comment: ";
    private static final String GET_COMMENT_MESSAGE = "Get comment: ";
    private static final String UPDATE_COMMENT_MESSAGE = "Update comment: ";
    private static final String DELETE_COMMENT_MESSAGE = "Delete comment: ";

    public Response<Void> createComment(CommentDTO commentDTO) {
        logger.info("{}{}", CREATE_COMMENT_MESSAGE, commentDTO);
        if (commentDTO == null) {
            logger.warn("{}{}", CREATE_COMMENT_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Question questionEntity = questionRepository.findQuestionById(commentDTO.getQuestionId());
        if (questionEntity == null) {
            logger.warn("{}{}", CREATE_COMMENT_MESSAGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(ServiceStatusCode.NOT_FOUND_STATUS, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        Comment comment = modelMapper.map(commentDTO, Comment.class);
        comment.setId(null);
        comment.setStatus(Status.ACTIVE_STATUS);
        commentRepository.save(comment);
        logger.info("Create question successfully");
        return new Response<>(ServiceStatusCode.OK_STATUS, ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Set<CommentDTO>> getAllCommentsOfAQuestion(QuestionDTO questionDTO) {
        logger.info("{}{}", GET_COMMENT_MESSAGE, questionDTO);
        if (questionDTO == null) {
            logger.warn("{}{}", GET_COMMENT_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Question questionEntity = questionRepository.findQuestionById(questionDTO.getId());
        if (questionEntity == null) {
            logger.warn("{}{}", GET_COMMENT_MESSAGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(ServiceStatusCode.NOT_FOUND_STATUS, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        var commentSet = questionEntity.getComments().stream().filter(comment -> comment.getStatus().equalsIgnoreCase("Active")).collect(Collectors.toSet());
        var commentDTOSet = commentSet.stream().map(comment -> modelMapper.map(comment, CommentDTO.class)).collect(Collectors.toSet());
        logger.info("Get all comment successfully.");
        return new Response<>(ServiceStatusCode.OK_STATUS, ServiceMessage.SUCCESS_MESSAGE.getMessage(), commentDTOSet);
    }

    public Response<CommentDTO> getCommentById(Integer id) {
        logger.info("{}{}", GET_COMMENT_MESSAGE, id);
        if (id == null) {
            logger.warn("{}{}", GET_COMMENT_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Comment commentEntity = commentRepository.findCommentById(id);
        if (commentEntity == null) {
            logger.warn("{}{}", GET_COMMENT_MESSAGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(ServiceStatusCode.NOT_FOUND_STATUS, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        CommentDTO commentDTO = modelMapper.map(commentEntity, CommentDTO.class);
        logger.info("Get comment successfully.");
        return new Response<>(ServiceStatusCode.OK_STATUS, ServiceMessage.SUCCESS_MESSAGE.getMessage(), commentDTO);
    }

    public Response<Void> updateContent(CommentDTO commentDTO, String content) {
        logger.info("{}{}", UPDATE_COMMENT_MESSAGE, commentDTO);
        if (commentDTO == null) {
            logger.warn("{}{}", UPDATE_COMMENT_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Comment commentEntity = commentRepository.findCommentToModifyById(commentDTO.getId());
        if (commentEntity == null) {
            logger.warn("{}{}", UPDATE_COMMENT_MESSAGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(ServiceStatusCode.NOT_FOUND_STATUS, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        if (content != null) {
            commentEntity.setContent(content);
        }
        commentRepository.save(commentEntity);
        logger.info("Update content of question successfully.");
        return new Response<>(ServiceStatusCode.OK_STATUS, ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> deleteComment(CommentDTO commentDTO) {
        logger.info("{}{}", DELETE_COMMENT_MESSAGE, commentDTO);
        if (commentDTO == null) {
            logger.warn("{}{}", DELETE_COMMENT_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Comment commentEntity = commentRepository.findCommentToModifyById(commentDTO.getId());
        if (commentEntity == null) {
            logger.warn("{}{}", DELETE_COMMENT_MESSAGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(ServiceStatusCode.NOT_FOUND_STATUS, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        commentEntity.setStatus(Status.INACTIVE_STATUS);
        commentRepository.save(commentEntity);
        logger.info("Delete Question successfully.");
        return new Response<>(ServiceStatusCode.OK_STATUS, ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> deleteAllCommentByAuthorEmail(String authorEmail) {
        logger.info("Delete all comment if author is banned.");
        if (authorEmail == null) {
            logger.warn("{}{}", DELETE_COMMENT_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        var comments = commentRepository.findCommentToDeleteByAuthorEmail(authorEmail);
        comments.forEach(comment -> {
            comment.setStatus(Status.INACTIVE_STATUS);
            commentRepository.save(comment);
        });

        logger.info("Delete all comments of a author successfully.");
        return new Response<>(ServiceStatusCode.OK_STATUS, ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }
}
