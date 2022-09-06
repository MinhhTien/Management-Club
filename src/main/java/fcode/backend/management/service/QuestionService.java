
package fcode.backend.management.service;

import fcode.backend.management.model.dto.QuestionDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.QuestionRepository;
import fcode.backend.management.repository.entity.Question;
import fcode.backend.management.service.constant.ServiceMessage;
import fcode.backend.management.service.constant.ServiceStatusCode;
import fcode.backend.management.service.constant.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;
@Service
public class QuestionService {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    ModelMapper modelMapper;

    private static final Logger logger = LogManager.getLogger(QuestionService.class);
    private static final String CREATE_QUESTION_MESSAGE = "Create question: ";
    private static final String GET_QUESTION_BY_ID_MESSAGE = "Get question by id: ";
    private static final String GET_QUESTION_BY_AUTHOR_MESSAGE = "Get question by author: ";
    private static final String APPROVE_QUESTION = "Approve question: ";
    private static final String UPDATE_QUESTION = "Update question: ";
    private static final String DELETE_QUESTION = "Delete question: ";

    public Response<Void> createQuestion(QuestionDTO questionDTO) {
        logger.info("{}{}", CREATE_QUESTION_MESSAGE, questionDTO);
        if (questionDTO == null) {
            logger.warn("{}{}", CREATE_QUESTION_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS.getCode(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Question question = modelMapper.map(questionDTO, Question.class);
        question.setId(null);
        question.setStatus(Status.PROCESSING_STATUS.getMessage());
        logger.info("{}{}", CREATE_QUESTION_MESSAGE, question);
        questionRepository.save(question);
        logger.info("Create question successfully");
        return new Response<>(ServiceStatusCode.OK_STATUS.getCode(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    @Transactional
    public Response<Set<QuestionDTO>> getAllQuestions() {
        logger.info("Get All Questions");
        Set<QuestionDTO> questionDTOSet = questionRepository.findAllQuestion(Status.ACTIVE_STATUS.getMessage()).stream().map(question -> modelMapper.map(question, QuestionDTO.class)).collect(Collectors.toSet());
        logger.info("Get All Questions successfully");
        return new Response<>(ServiceStatusCode.OK_STATUS.getCode(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), questionDTOSet);
    }
    public Response<QuestionDTO> getQuestionById(Integer id) {
        logger.info("{}{}", GET_QUESTION_BY_ID_MESSAGE, id);
        if (id == null) {
            logger.warn("{}{}", GET_QUESTION_BY_ID_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS.getCode(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Question question = questionRepository.findQuestionById(id, Status.ACTIVE_STATUS.getMessage());
        if (question == null) {
            logger.warn("{}{}", GET_QUESTION_BY_ID_MESSAGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(ServiceStatusCode.NOT_FOUND_STATUS.getCode(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        QuestionDTO questionDTO = modelMapper.map(question, QuestionDTO.class);
        logger.info("Get question by Id successfully");
        return new Response<>(ServiceStatusCode.OK_STATUS.getCode(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), questionDTO);
    }

    @Transactional
    public Response<Set<QuestionDTO>> getQuestionByAuthor(String authorEmail) {
        logger.info("{}{}", GET_QUESTION_BY_AUTHOR_MESSAGE, authorEmail);
        if (authorEmail == null) {
            logger.warn("{}{}", GET_QUESTION_BY_AUTHOR_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS.getCode(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Set<Question> questions = questionRepository.findQuestionByAuthorEmail(authorEmail, Status.ACTIVE_STATUS.getMessage());
        if (questions.isEmpty()) {
            logger.warn("{}{}", GET_QUESTION_BY_AUTHOR_MESSAGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(ServiceStatusCode.NOT_FOUND_STATUS.getCode(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        Set<QuestionDTO> questionDTOSet = questions.stream().map(question -> modelMapper.map(question, QuestionDTO.class)).collect(Collectors.toSet());
        logger.info("Get question by author successfully");
        return new Response<>(ServiceStatusCode.OK_STATUS.getCode(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), questionDTOSet);
    }


    public Response<Void> approveQuestion(Integer id) {
        logger.info("{}{}", APPROVE_QUESTION, id);

        Question questionEntity = questionRepository.findQuestionToApproveById(id, Status.PROCESSING_STATUS.getMessage());
        if (questionEntity == null) {
            logger.warn("{}{}", APPROVE_QUESTION, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(ServiceStatusCode.NOT_FOUND_STATUS.getCode(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }

        questionEntity.setStatus(Status.ACTIVE_STATUS.getMessage());
        questionRepository.save(questionEntity);
        logger.info("Approve question successfully");
        return new Response<>(ServiceStatusCode.OK_STATUS.getCode(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> disapproveQuestion(Integer id) {
        logger.info("{}{}", APPROVE_QUESTION, id);

        Question questionEntity = questionRepository.findQuestionToApproveById(id, Status.PROCESSING_STATUS.getMessage());
        if (questionEntity == null) {
            logger.warn("{}{}", APPROVE_QUESTION, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(ServiceStatusCode.NOT_FOUND_STATUS.getCode(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }

        questionEntity.setStatus(Status.INACTIVE_STATUS.getMessage());
        questionRepository.save(questionEntity);
        logger.info("Disapprove question successfully");
        return new Response<>(ServiceStatusCode.OK_STATUS.getCode(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> updateContent(QuestionDTO questionDTO, String content) {
        logger.info("{}{}", UPDATE_QUESTION, questionDTO);
        if (questionDTO == null) {
            logger.warn("{}{}", UPDATE_QUESTION, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS.getCode(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Question questionEntity = questionRepository.findQuestionToModifyById(questionDTO.getId(), Status.INACTIVE_STATUS.getMessage());
        if (questionEntity == null) {
            logger.warn("{}{}", UPDATE_QUESTION, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(ServiceStatusCode.NOT_FOUND_STATUS.getCode(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        if (content != null) {
            questionEntity .setContent(content);
        }
        questionRepository.save(questionEntity);
        logger.info("Update content of question successfully.");
        return new Response<>(ServiceStatusCode.OK_STATUS.getCode(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> updateTitle(QuestionDTO questionDTO, String title) {
        logger.info("{}{}", UPDATE_QUESTION, questionDTO);
        if (questionDTO == null) {
            logger.warn("{}{}", UPDATE_QUESTION, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS.getCode(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Question questionEntity = questionRepository.findQuestionToModifyById(questionDTO.getId(), Status.INACTIVE_STATUS.getMessage());
        if (questionEntity == null) {
            logger.warn("{}{}", UPDATE_QUESTION, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(ServiceStatusCode.NOT_FOUND_STATUS.getCode(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        if (title != null) {
            questionEntity.setTitle(title);
        }
        questionRepository.save(questionEntity);
        logger.info("Update title of question successfully.");
        return new Response<>(ServiceStatusCode.OK_STATUS.getCode(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> updateQuestion(QuestionDTO questionDTO, String title, String content) {
        logger.info("{}{}", UPDATE_QUESTION, questionDTO);
        if (questionDTO == null) {
            logger.warn("{}{}", UPDATE_QUESTION, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS.getCode(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Question questionEntity = questionRepository.findQuestionToModifyById(questionDTO.getId(), Status.INACTIVE_STATUS.getMessage());
        if (questionEntity == null) {
            logger.warn("{}{}", UPDATE_QUESTION, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(ServiceStatusCode.NOT_FOUND_STATUS.getCode(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        if (title != null)
            questionEntity.setTitle(title);
        if (content != null)
            questionEntity.setContent(content);

        questionRepository.save(questionEntity);
        logger.info("Update question of question successfully.");
        return new Response<>(ServiceStatusCode.OK_STATUS.getCode(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> deleteQuestion(Integer id) {
        logger.info("{}{}", DELETE_QUESTION, id);

        Question questionEntity = questionRepository.findQuestionToModifyById(id, Status.INACTIVE_STATUS.getMessage());
        if (questionEntity == null) {
            logger.warn("{}{}", DELETE_QUESTION, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(ServiceStatusCode.NOT_FOUND_STATUS.getCode(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        questionEntity.setStatus(Status.INACTIVE_STATUS.getMessage());
        questionRepository.save(questionEntity);
        logger.info("Delete Question successfully.");
        return new Response<>(ServiceStatusCode.OK_STATUS.getCode(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }
    @Transactional
    public Response<Void> deleteQuestionByAuthorEmail(String authorEmail) {
        logger.info("Delete all question if author is banned.");
        if (authorEmail == null) {
            logger.warn("{}{}", DELETE_QUESTION, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS.getCode(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        var questions = questionRepository.findQuestionToDeleteByAuthorEmail(authorEmail, Status.INACTIVE_STATUS.getMessage());
        questions.forEach(question -> {
            question.setStatus(Status.INACTIVE_STATUS.getMessage());
            questionRepository.save(question);
        });

        logger.info("Delete all question of a author successfully.");
        return new Response<>(ServiceStatusCode.OK_STATUS.getCode(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }


}
