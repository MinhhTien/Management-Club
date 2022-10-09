package fcode.backend.management.service;

import fcode.backend.management.config.interceptor.Status;
import fcode.backend.management.model.dto.ChallengeDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.ChallengeRepository;
import fcode.backend.management.repository.entity.Challenge;
import fcode.backend.management.service.constant.ServiceMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChallengeServices {
    @Autowired
    ChallengeRepository challengeRepository;
    @Autowired
    ModelMapper modelMapper;

    private static final Logger logger = LogManager.getLogger(ChallengeServices.class);
    private static final String CREATE_CHALLENGE = "Create challenge: ";
    private static final String UPDATE_CHALLENGE = "Update challenge: ";
    private static final String DELETE_CHALLENGE = "Delete challenge: ";
    private static final String GET_CHALLENGE_BY_ID = "Get challenge by id: ";
    private static final String GET_CHALLENGE_BY_TITLE = "Get challenge by title: ";

    public Response<List<ChallengeDTO>> getAllChallenge() {
        logger.info("Get all challenge");
        List<ChallengeDTO> challengeDTOList = challengeRepository.findAllChallenge(Status.ACTIVE.toString()).stream()
                .map(challenge -> modelMapper.map(challenge, ChallengeDTO.class)).collect(Collectors.toList());
        logger.info("Get all challenge successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), challengeDTOList);
    }

    public Response<ChallengeDTO> getChallengeById(Integer id) {
        logger.info("Get challenge by ID: {}", id);
        if (id == null) {
            logger.warn("{}{}", GET_CHALLENGE_BY_ID, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if (challengeRepository.findChallengeById(id, Status.ACTIVE.toString()) == null) {
            logger.warn("{}{}", GET_CHALLENGE_BY_ID, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        Challenge challenge = challengeRepository.findChallengeById(id, Status.ACTIVE.toString());
        ChallengeDTO challengeDTO = modelMapper.map(challenge, ChallengeDTO.class);
        logger.info("{}{}", GET_CHALLENGE_BY_ID, ServiceMessage.SUCCESS_MESSAGE);
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), challengeDTO);
    }

    public Response<ChallengeDTO> getChallengeByTitle(String title) {
        logger.info("Get challenge by title: {}", title);
        if (title == null) {
            logger.warn("{}{}", GET_CHALLENGE_BY_TITLE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if (challengeRepository.findChallengeByTitle(title, Status.ACTIVE.toString()) == null) {
            logger.warn("{}{}", GET_CHALLENGE_BY_TITLE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        Challenge challenge = challengeRepository.findChallengeByTitle(title, Status.ACTIVE.toString());
        ChallengeDTO challengeDTO = modelMapper.map(challenge, ChallengeDTO.class);
        logger.info("{}{}", GET_CHALLENGE_BY_TITLE, ServiceMessage.SUCCESS_MESSAGE);
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), challengeDTO);
    }

    @Transactional
    public Response<Void> createChallenge(ChallengeDTO challengeDTO) {
        logger.info("{}{}", CREATE_CHALLENGE, challengeDTO);
        if (challengeDTO == null) {
            logger.warn("{}{}", CREATE_CHALLENGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }

        if (challengeRepository.findChallengeByTitle(challengeDTO.getTitle(), Status.ACTIVE.toString()) != null) {
            logger.warn("{}{}", CREATE_CHALLENGE, "Challenge is already exist");
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Challenge is already exist");
        }

        Challenge challenge = modelMapper.map(challengeDTO, Challenge.class);
        challenge.setStatus(Status.ACTIVE);
        logger.info("{}{}", CREATE_CHALLENGE, challenge);
        challengeRepository.save(challenge);
        logger.info("Create challenge successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    @Transactional
    public Response<Void> updateChallenge(ChallengeDTO challengeDTO) {
        logger.info("{}{}", UPDATE_CHALLENGE, challengeDTO);
        if (challengeDTO == null) {
            logger.warn("{}{}", UPDATE_CHALLENGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Challenge challengeEntity = challengeRepository.findChallengeById(challengeDTO.getId(), Status.ACTIVE.toString());
        if (challengeEntity == null) {
            logger.warn("{}{}", UPDATE_CHALLENGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }

        if (challengeDTO.getTitle() != null) {
            challengeEntity.setTitle(challengeDTO.getTitle());
        } else challengeEntity.setTitle(challengeEntity.getTitle());

        if (challengeDTO.getDescription() != null) {
            challengeEntity.setDescription(challengeDTO.getDescription());
        } else challengeEntity.setDescription(challengeEntity.getDescription());

        if (challengeDTO.getStartTime() != null) {
            challengeEntity.setStartTime(challengeDTO.getStartTime());
        } else challengeEntity.setStartTime(challengeEntity.getStartTime());

        if (challengeDTO.getEndTime() != null) {
            challengeEntity.setEndTime(challengeDTO.getEndTime());
        } else challengeEntity.setEndTime(challengeEntity.getEndTime());
        challengeRepository.save(challengeEntity);
        logger.info("Update challenge successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    @Transactional
    public Response<Void> deleteChallenge(Integer id) {
        logger.info("{}{}", DELETE_CHALLENGE, id);
        if (id == null) {
            logger.warn("{}{}", DELETE_CHALLENGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Challenge challengeEntity = challengeRepository.findChallengeById(id, Status.ACTIVE.toString());
        if (challengeEntity == null) {
            logger.warn("{}{}", DELETE_CHALLENGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        challengeEntity.setStatus(Status.INACTIVE);
        challengeRepository.save(challengeEntity);
        logger.info("Delete challenge successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

}
