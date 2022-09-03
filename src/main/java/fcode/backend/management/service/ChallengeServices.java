package fcode.backend.management.service;

import fcode.backend.management.model.dto.ChallengeDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.ChallengeRepository;
import fcode.backend.management.repository.entity.Challenge;
import fcode.backend.management.service.constant.ServiceMessage;
import fcode.backend.management.service.constant.ServiceStatusCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ChallengeServices {
    @Autowired
    ChallengeRepository challengeRepository;
    @Autowired
    ModelMapper modelMapper;

    private static final Logger logger = LogManager.getLogger(ChallengeServices.class);
    private static final String CREATE_CHALLENGE = "Create challenge: ";
    private static final String UPDATE_CHALLENGE = "Update challenge: ";
    private static final String DELETE_CHALLENGE = "Delete challenge: ";

    public Response<List<ChallengeDTO>> getAllChallenge() {
        logger.info("Get all challenge");
        List<ChallengeDTO> challengeDTOList = challengeRepository.findAllChallenge().stream()
                .map(challenge -> modelMapper.map(challenge, ChallengeDTO.class)).collect(Collectors.toList());
        logger.info("Get all challenge successfully");
        return new Response<>(ServiceStatusCode.OK_STATUS, ServiceMessage.SUCCESS_MESSAGE.getMessage(), challengeDTOList);
    }

    @Transactional
    public Response<Void> createChallenge(ChallengeDTO challengeDTO) {
        logger.info("{}{}", CREATE_CHALLENGE, challengeDTO);
        if(challengeDTO == null) {
            logger.warn("{}{}", CREATE_CHALLENGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if(challengeRepository.findChallengeByTitle(challengeDTO.getTitle()) != null) {
            logger.warn("{}{}", CREATE_CHALLENGE, "Challenge is exited.");
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS, "Challenge is exited.");
        }
        Challenge challenge = modelMapper.map(challengeDTO, Challenge.class);
        challenge.setStatus("available");
        logger.info("{}{}", CREATE_CHALLENGE, challenge);
        challengeRepository.save(challenge);
        logger.info("Create challenge successfully");
        return new Response<>(ServiceStatusCode.OK_STATUS, ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    @Transactional
    public Response<Void> updateChallenge(ChallengeDTO challengeDTO, String title, String description, String register_url) {
        logger.info("{}{}", UPDATE_CHALLENGE, challengeDTO);
        if (challengeDTO == null) {
            logger.warn("{}{}", UPDATE_CHALLENGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Challenge challengeEntity = challengeRepository.findChallengeById(challengeDTO.getId());
        if (challengeEntity == null) {
            logger.warn("{}{}", UPDATE_CHALLENGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(ServiceStatusCode.NOT_FOUND_STATUS, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        if (title != null) {
            challengeEntity.setTitle(title);
        } else challengeEntity.setTitle(challengeEntity.getTitle());
        if (description != null) {
            challengeEntity.setDescription(description);
        } else challengeEntity.setDescription(challengeEntity.getDescription());
        if (register_url != null) {
            challengeEntity.setRegister_url(register_url);
        } else challengeEntity.setRegister_url(challengeEntity.getRegister_url());

        challengeRepository.save(challengeEntity);
        logger.info("Update challenge successfully");
        return new Response<>(ServiceStatusCode.OK_STATUS, ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    @Transactional
    public Response<Void> deleteChallenge(ChallengeDTO challengeDTO) {
        logger.info("{}{}", DELETE_CHALLENGE, challengeDTO);
        if (challengeDTO == null) {
            logger.warn("{}{}", DELETE_CHALLENGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Challenge challengeEntity = challengeRepository.findChallengeById(challengeDTO.getId());
        if (challengeEntity == null) {
            logger.warn("{}{}", DELETE_CHALLENGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(ServiceStatusCode.NOT_FOUND_STATUS, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        challengeEntity.setStatus("unavailable");
        challengeRepository.save(challengeEntity);
        logger.info("Delete challenge successfully");
        return new Response<>(ServiceStatusCode.OK_STATUS, ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }


}
