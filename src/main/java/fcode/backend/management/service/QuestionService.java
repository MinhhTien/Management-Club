
package fcode.backend.management.service;

import fcode.backend.management.config.Role;
import fcode.backend.management.model.dto.QuestionDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.MemberRepository;
import fcode.backend.management.repository.QuestionRepository;
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
public class QuestionService {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    MemberRepository memberRepository;
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
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Question question = modelMapper.map(questionDTO, Question.class);
        question.setId(null);
        question.setStatus(Status.PROCESSING);
        logger.info("{}{}", CREATE_QUESTION_MESSAGE, question);
        questionRepository.save(question);
        logger.info("Create question successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    @Transactional
    public Response<Set<QuestionDTO>> getAllQuestions() {
        logger.info("Get All Questions");
        Set<QuestionDTO> questionDTOSet = questionRepository.findQuestionByStatus(Status.ACTIVE).stream().map(question -> modelMapper.map(question, QuestionDTO.class)).collect(Collectors.toSet());
        logger.info("Get All Questions successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), questionDTOSet);
    }

    @Transactional
    public Response<Set<QuestionDTO>> getProcessingQuestions() {
        logger.info("Get All Processing Questions");
        Set<QuestionDTO> questionDTOSet = questionRepository.findQuestionByStatus(Status.PROCESSING).stream().map(question -> modelMapper.map(question, QuestionDTO.class)).collect(Collectors.toSet());
        logger.info("Get All Processing Questions successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), questionDTOSet);
    }

    @Transactional
    public Response<Set<QuestionDTO>> getInactiveQuestions() {
        logger.info("Get All Processing Questions");
        Set<QuestionDTO> questionDTOSet = questionRepository.findQuestionByStatus(Status.INACTIVE).stream().map(question -> modelMapper.map(question, QuestionDTO.class)).collect(Collectors.toSet());
        logger.info("Get All Processing Questions successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), questionDTOSet);
    }

    public Response<QuestionDTO> getQuestionById(Integer id) {
        logger.info("{}{}", GET_QUESTION_BY_ID_MESSAGE, id);
        if (id == null) {
            logger.warn("{}{}", GET_QUESTION_BY_ID_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Question question = questionRepository.findQuestionByIdAndStatus(id, Status.ACTIVE);
        if (question == null) {
            logger.warn("{}{}", GET_QUESTION_BY_ID_MESSAGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        QuestionDTO questionDTO = modelMapper.map(question, QuestionDTO.class);
        logger.info("Get question by Id successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), questionDTO);
    }


    @Transactional
    public Response<Set<QuestionDTO>> getQuestionByAuthor(String authorEmail) {
        logger.info("{}{}", GET_QUESTION_BY_AUTHOR_MESSAGE, authorEmail);
        if (authorEmail == null) {
            logger.warn("{}{}", GET_QUESTION_BY_AUTHOR_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Set<Question> questions = questionRepository.findQuestionByAuthorEmailAndStatus(authorEmail, Status.ACTIVE);
        if (questions.isEmpty()) {
            logger.warn("{}{}", GET_QUESTION_BY_AUTHOR_MESSAGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        Set<QuestionDTO> questionDTOSet = questions.stream().map(question -> modelMapper.map(question, QuestionDTO.class)).collect(Collectors.toSet());
        logger.info("Get question by author successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), questionDTOSet);
    }


    public Response<Void> approveQuestion(Integer id) {
        logger.info("{}{}", APPROVE_QUESTION, id);

        Question questionEntity = questionRepository.findQuestionByIdAndStatus(id, Status.PROCESSING);
        if (questionEntity == null) {
            logger.warn("{}{}", APPROVE_QUESTION, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }

        questionEntity.setStatus(Status.ACTIVE);
        questionRepository.save(questionEntity);
        logger.info("Approve question successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    @Transactional
    public Response<Void> approveAll() {
        logger.info("Approve all Article");
        Set<Question> questionSet = questionRepository.findQuestionByStatus(Status.PROCESSING);
        questionSet.forEach(question -> {
            question.setStatus(Status.ACTIVE);
            questionRepository.save(question);
        });
        logger.info("Approve all articles successfully.");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> disapproveQuestion(Integer id) {
        logger.info("{}{}", APPROVE_QUESTION, id);

        Question questionEntity = questionRepository.findQuestionByIdAndStatus(id, Status.PROCESSING);
        if (questionEntity == null) {
            logger.warn("{}{}", APPROVE_QUESTION, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }

        questionEntity.setStatus(Status.INACTIVE);
        questionRepository.save(questionEntity);
        logger.info("Disapprove question successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    @Transactional
    public Response<Void> disapproveAll() {
        logger.info("Disapprove all Article");
        Set<Question> questionSet = questionRepository.findQuestionByStatus(Status.PROCESSING);
        questionSet.forEach(question -> {
            question.setStatus(Status.INACTIVE);
            questionRepository.save(question);
        });
        logger.info("Disapprove all articles successfully.");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }


    public Response<Void> updateQuestion(QuestionDTO questionDTO, String userEmail) {
        logger.info("{}{}", UPDATE_QUESTION, questionDTO);
        if (questionDTO == null) {
            logger.warn("{}{}", UPDATE_QUESTION, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Question questionEntity = questionRepository.findQuestionByIdAndStatusIsNot(questionDTO.getId(), Status.INACTIVE);
        if (questionEntity == null) {
            logger.warn("{}{}", UPDATE_QUESTION, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        if (!userEmail.equals(questionEntity.getAuthorEmail())) {
            logger.warn("{}{}", UPDATE_QUESTION, ServiceMessage.FORBIDDEN_MESSAGE);
            return new Response<>(HttpStatus.FORBIDDEN.value(), ServiceMessage.FORBIDDEN_MESSAGE.getMessage());
        }
        if (questionDTO.getTitle() != null)
            questionEntity.setTitle(questionDTO.getTitle());
        if (questionDTO.getContent() != null)
            questionEntity.setContent(questionDTO.getContent());

        questionRepository.save(questionEntity);
        logger.info("Update question of question successfully.");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> deleteQuestion(Integer id, String userEmail) {
        logger.info("{}{}", DELETE_QUESTION, id);

        Question questionEntity = questionRepository.findQuestionByIdAndStatusIsNot(id, Status.INACTIVE);
        if (questionEntity == null) {
            logger.warn("{}{}", DELETE_QUESTION, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        Member user = memberRepository.findMemberByEmail(userEmail);
        if (user == null || !user.getRole().equals(Role.MANAGER)) {
            if (!userEmail.equals(questionEntity.getAuthorEmail())) {
                logger.warn("{}{}", DELETE_QUESTION, ServiceMessage.FORBIDDEN_MESSAGE);
                return new Response<>(HttpStatus.FORBIDDEN.value(), ServiceMessage.FORBIDDEN_MESSAGE.getMessage());
            }
        }
        questionEntity.setStatus(Status.INACTIVE);
        questionRepository.save(questionEntity);
        if (user != null && user.getRole().equals(Role.MANAGER)) logger.info("Delete Question by manager successfully. Deleter id: {}", user.getId());
        else logger.info("Delete question successfully.");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }


}
