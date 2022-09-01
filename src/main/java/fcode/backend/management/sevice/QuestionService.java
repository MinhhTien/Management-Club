
package fcode.backend.management.sevice;

import fcode.backend.management.model.dto.CommentDTO;
import fcode.backend.management.model.dto.QuestionDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.QuestionRepository;
import fcode.backend.management.repository.entity.Question;
import fcode.backend.management.sevice.constant.ServiceMessage;
import fcode.backend.management.sevice.constant.ServiceStatusCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private static final String GET_COMMENT_OF_QUESTION = "Get comment of the question: ";
    private static final String APPROVE_QUESTION = "Approve question: ";
    private static final String UPDATE_QUESTION = "Update question: ";
    private static final String DELETE_QUESTION = "Delete question: ";
    public Response<Void> createQuestion(QuestionDTO questionDTO) {
        logger.info("{}{}", CREATE_QUESTION_MESSAGE, questionDTO);
        if (questionDTO == null) {
            logger.warn("{}{}", CREATE_QUESTION_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Question question = modelMapper.map(questionDTO, Question.class);
        question.setStatus("Processing");
        questionRepository.save(question);
        logger.info("Create question successfully");
        return new Response<>(ServiceStatusCode.OK_STATUS, ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Set<QuestionDTO>> getAllQuestions() {
        logger.info("Get All Questions");
        Set<QuestionDTO> questionDTOSet = questionRepository.findAllQuestion().stream().map(question -> modelMapper.map(question, QuestionDTO.class)).collect(Collectors.toSet());
        logger.info("Get All Questions successfully");
        return new Response<>(ServiceStatusCode.OK_STATUS, ServiceMessage.SUCCESS_MESSAGE.getMessage(), questionDTOSet);
    }
    public Response<QuestionDTO> getQuestionById(Integer id) {
        logger.info("{}{}", GET_QUESTION_BY_ID_MESSAGE, id);
        if (id == null) {
            logger.warn("{}{}", GET_QUESTION_BY_ID_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Question question = questionRepository.findQuestionById(id);
        if (question == null) {
            logger.warn("{}{}", GET_QUESTION_BY_ID_MESSAGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(ServiceStatusCode.NOT_FOUND_STATUS, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        QuestionDTO questionDTO = modelMapper.map(question, QuestionDTO.class);
        logger.info("Get question by Id successfully");
        return new Response<>(ServiceStatusCode.OK_STATUS, ServiceMessage.SUCCESS_MESSAGE.getMessage(), questionDTO);
    }

    public Response<Set<QuestionDTO>> getQuestionByAuthor(String authorEmail) {
        logger.info("{}{}", GET_QUESTION_BY_AUTHOR_MESSAGE, authorEmail);
        if (authorEmail == null) {
            logger.warn("{}{}", GET_QUESTION_BY_AUTHOR_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Set<Question> questions = questionRepository.findQuestionByAuthorEmail(authorEmail);
        if (questions == null) {
            logger.warn("{}{}", GET_QUESTION_BY_AUTHOR_MESSAGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(ServiceStatusCode.NOT_FOUND_STATUS, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        Set<QuestionDTO> questionDTOSet = questions.stream().map(question -> modelMapper.map(question, QuestionDTO.class)).collect(Collectors.toSet());
        logger.info("Get question by author successfully");
        return new Response<>(ServiceStatusCode.OK_STATUS, ServiceMessage.SUCCESS_MESSAGE.getMessage(), questionDTOSet);
    }

    public Response<Set<CommentDTO>> getAllCommentsOfAQuestion(QuestionDTO questionDTO) {
        logger.info("{}{}", GET_COMMENT_OF_QUESTION, questionDTO);
        if (questionDTO == null) {
            logger.warn("{}{}", GET_COMMENT_OF_QUESTION, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Question questionEntity = questionRepository.findQuestionById(questionDTO.getId());
        if (questionEntity == null) {
            logger.warn("{}{}", GET_COMMENT_OF_QUESTION, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(ServiceStatusCode.NOT_FOUND_STATUS, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        var commentSet = questionEntity.getComments().stream().filter(comment -> comment.getStatus().equalsIgnoreCase("Active")).collect(Collectors.toSet());
        var commentDTOSet = commentSet.stream().map(comment -> modelMapper.map(comment, CommentDTO.class)).collect(Collectors.toSet());
        logger.info("Get all comment successfully.");
        return new Response<>(ServiceStatusCode.OK_STATUS, ServiceMessage.SUCCESS_MESSAGE.getMessage(), commentDTOSet);
    }
    public Response<Void> approveQuestion(QuestionDTO questionDTO) {
        logger.info("{}{}", APPROVE_QUESTION, questionDTO);
        if (questionDTO == null) {
            logger.warn("{}{}", APPROVE_QUESTION, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Question questionEntity = questionRepository.findQuestionById(questionDTO.getId());
        if (questionEntity == null) {
            logger.warn("{}{}", APPROVE_QUESTION, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(ServiceStatusCode.NOT_FOUND_STATUS, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }

        questionEntity.setStatus("Active");
        questionRepository.save(questionEntity);
        logger.info("Approve question successfully");
        return new Response<>(ServiceStatusCode.OK_STATUS, ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> updateContent(QuestionDTO questionDTO, String content) {
        logger.info("{}{}", UPDATE_QUESTION, questionDTO);
        if (questionDTO == null) {
            logger.warn("{}{}", UPDATE_QUESTION, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Question questionEntity = questionRepository.findQuestionById(questionDTO.getId());
        if (questionEntity == null) {
            logger.warn("{}{}", UPDATE_QUESTION, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(ServiceStatusCode.NOT_FOUND_STATUS, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        if (content != null)
            questionEntity .setContent(content);
        questionRepository.save(questionEntity);
        logger.info("Update content of question successfully.");
        return new Response<>(ServiceStatusCode.OK_STATUS, ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> updateTitle(QuestionDTO questionDTO, String title) {
        logger.info("{}{}", UPDATE_QUESTION, questionDTO);
        if (questionDTO == null) {
            logger.warn("{}{}", UPDATE_QUESTION, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Question questionEntity = questionRepository.findQuestionById(questionDTO.getId());
        if (questionEntity == null) {
            logger.warn("{}{}", UPDATE_QUESTION, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(ServiceStatusCode.NOT_FOUND_STATUS, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        if (title != null)
            questionEntity.setTitle(title);
        questionRepository.save(questionEntity);
        logger.info("Update title of question successfully.");
        return new Response<>(ServiceStatusCode.OK_STATUS, ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> updateQuestion(QuestionDTO questionDTO, String title, String content) {
        logger.info("{}{}", UPDATE_QUESTION, questionDTO);
        if (questionDTO == null) {
            logger.warn("{}{}", UPDATE_QUESTION, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Question questionEntity = questionRepository.findQuestionById(questionDTO.getId());
        if (questionEntity == null) {
            logger.warn("{}{}", UPDATE_QUESTION, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(ServiceStatusCode.NOT_FOUND_STATUS, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        if (title != null)
            questionEntity.setTitle(title);
        if (content != null)
            questionEntity.setContent(content);
        questionRepository.save(questionEntity);
        logger.info("Update question of question successfully.");
        return new Response<>(ServiceStatusCode.OK_STATUS, ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> deleteQuestion(QuestionDTO questionDTO) {
        logger.info("{}{}", DELETE_QUESTION, questionDTO);
        if (questionDTO == null) {
            logger.warn("{}{}", DELETE_QUESTION, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Question questionEntity = questionRepository.findQuestionById(questionDTO.getId());
        if (questionEntity == null) {
            logger.warn("{}{}", DELETE_QUESTION, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(ServiceStatusCode.NOT_FOUND_STATUS, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        questionEntity.setStatus("Inactive");
        logger.info("Delete Question successfully.");
        return new Response<>(ServiceStatusCode.OK_STATUS, ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }
    public Response<Void> deleteQuestionByAuthorEmail(String authorEmail) {
        logger.info("Delete all question if author is banned.");
        if (authorEmail == null) {
            logger.warn("{}{}", DELETE_QUESTION, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        var questions = questionRepository.findQuestionByAuthorEmail(authorEmail);
        questions.forEach(question -> question.setStatus("Inactive"));
        logger.info("Delete all question of a author successfully.");
        return new Response<>(ServiceStatusCode.OK_STATUS, ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

}
