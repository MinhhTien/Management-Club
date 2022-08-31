
package fcode.backend.management.sevice;

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

    public Response<QuestionDTO> getQuestionById(Integer id) {
        logger.info("{}{}", GET_QUESTION_BY_ID_MESSAGE, id);
        if (id == null) {
            logger.warn("{}{}", GET_QUESTION_BY_ID_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Question question = questionRepository.findOneById(id);
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
    
}
