package fcode.backend.management.service;

import fcode.backend.management.config.Role;
import fcode.backend.management.model.dto.CommentDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.CommentRepository;
import fcode.backend.management.repository.MemberRepository;
import fcode.backend.management.repository.QuestionRepository;
import fcode.backend.management.repository.entity.Comment;
import fcode.backend.management.repository.entity.Member;
import fcode.backend.management.repository.entity.Question;
import fcode.backend.management.service.constant.ServiceMessage;
import fcode.backend.management.service.constant.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;
@Service
public class CommentService {


    @Autowired
    CommentRepository commentRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    MemberRepository memberRepository;
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
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Question questionEntity = questionRepository.findQuestionByIdAndStatus(commentDTO.getQuestionId(), Status.ACTIVE);
        if (questionEntity == null) {
            logger.warn("{}{}", CREATE_COMMENT_MESSAGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        Comment comment = modelMapper.map(commentDTO, Comment.class);
        comment.setId(null);
        comment.setStatus(Status.ACTIVE);
        commentRepository.save(comment);
        logger.info("Create question successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }
    @Transactional
    public Response<Set<CommentDTO>> getAllCommentsOfAQuestion(Integer questionId) {
        logger.info("{}{}", GET_COMMENT_MESSAGE, questionId);

        Question questionEntity = questionRepository.findQuestionByIdAndStatus(questionId, Status.ACTIVE);

        if (questionEntity == null) {
            logger.warn("{}{}", GET_COMMENT_MESSAGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }

        var commentSet = questionEntity.getComments().stream().filter(comment -> comment.getStatus().equals(Status.ACTIVE)).collect(Collectors.toSet());
        var commentDTOSet = commentSet.stream().map(comment -> modelMapper.map(comment, CommentDTO.class)).collect(Collectors.toSet());
        logger.info("Get all comment successfully.");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), commentDTOSet);
    }

    public Response<CommentDTO> getCommentById(Integer id) {
        logger.info("{}{}", GET_COMMENT_MESSAGE, id);
        if (id == null) {
            logger.warn("{}{}", GET_COMMENT_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Comment commentEntity = commentRepository.findCommentByIdAndStatus(id, Status.ACTIVE);
        if (commentEntity == null) {
            logger.warn("{}{}", GET_COMMENT_MESSAGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        CommentDTO commentDTO = modelMapper.map(commentEntity, CommentDTO.class);
        logger.info("Get comment successfully.");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), commentDTO);
    }

    public Response<Void> updateContent(CommentDTO commentDTO, String userEmail) {
        logger.info("{}{}", UPDATE_COMMENT_MESSAGE, commentDTO);

        if (commentDTO == null) {
            logger.warn("{}{}", UPDATE_COMMENT_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }

        Comment commentEntity = commentRepository.findCommentByIdAndStatusIsNot(commentDTO.getId(), Status.INACTIVE);

        if (commentEntity == null) {
            logger.warn("{}{}", UPDATE_COMMENT_MESSAGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        if (!userEmail.equalsIgnoreCase(commentEntity.getAuthorEmail())) {
            logger.warn("{}{}", UPDATE_COMMENT_MESSAGE, ServiceMessage.FORBIDDEN_MESSAGE);
            return new Response<>(HttpStatus.FORBIDDEN.value(), ServiceMessage.FORBIDDEN_MESSAGE.getMessage());
        }
        if (commentDTO.getContent() != null) {
            commentEntity.setContent(commentDTO.getContent());
        }
        commentRepository.save(commentEntity);
        logger.info("Update content of question successfully.");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> deleteComment(Integer id, String userEmail) {
        logger.info("{}{}", DELETE_COMMENT_MESSAGE, id);

        Comment commentEntity = commentRepository.findCommentByIdAndStatusIsNot(id, Status.INACTIVE);
        if (commentEntity == null) {
            logger.warn("{}{}", DELETE_COMMENT_MESSAGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }

        Member user = memberRepository.findMemberByEmail(userEmail);
        if (user == null || !user.getRole().equals(Role.MANAGER)) {
            if (!userEmail.equals(commentEntity.getAuthorEmail())) {
                logger.warn("{}{}", DELETE_COMMENT_MESSAGE, ServiceMessage.FORBIDDEN_MESSAGE);
                return new Response<>(HttpStatus.FORBIDDEN.value(), ServiceMessage.FORBIDDEN_MESSAGE.getMessage());
            }
        }
        commentEntity.setStatus(Status.INACTIVE);
        commentRepository.save(commentEntity);
        if (user != null && user.getRole().equals(Role.MANAGER)) logger.info("Delete Comment by Manager successfully. Deleter id: {}", user.getId());
        else logger.info("Delete Comment successfully.");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    @Transactional
    public Response<Void> deleteAllCommentByAuthorEmail(String authorEmail) {
        logger.info("Delete all comment if author is banned.");
        if (authorEmail == null) {
            logger.warn("{}{}", DELETE_COMMENT_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        var comments = commentRepository.findCommentByAuthorEmailAndStatusIsNot(authorEmail, Status.INACTIVE);
        comments.forEach(comment -> {
            comment.setStatus(Status.INACTIVE);
            commentRepository.save(comment);
        });

        logger.info("Delete all comments of a author successfully.");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }
}
