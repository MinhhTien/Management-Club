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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
    private static final String RESTORE_CHALLENGE = "Restore challenge";

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

        Challenge challenge = modelMapper.map(challengeDTO, Challenge.class);
        challenge.setCreated_time(new Date());
        challenge.setUpdated_time(new Date());
        challenge.setStatus("available");
        logger.info("{}{}", CREATE_CHALLENGE, challenge);
        challengeRepository.save(challenge);
        logger.info("Create challenge successfully");
        return new Response<>(ServiceStatusCode.OK_STATUS, ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    @Transactional
    public Response<Void> updateChallenge(Integer id, String title, String description, String register_url) {
        logger.info("{}{}", UPDATE_CHALLENGE, id);
        if (id == null) {
            logger.warn("{}{}", UPDATE_CHALLENGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Challenge challengeEntity = challengeRepository.findChallengeById(id);
        if (challengeEntity == null) {
            logger.warn("{}{}", UPDATE_CHALLENGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(ServiceStatusCode.NOT_FOUND_STATUS, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        boolean flag = false;
        if (title != null) {
            challengeEntity.setTitle(title);
            flag = true;
        } else challengeEntity.setTitle(challengeEntity.getTitle());
        if (description != null) {
            challengeEntity.setDescription(description);
            flag = true;
        } else challengeEntity.setDescription(challengeEntity.getDescription());
        if (register_url != null) {
            challengeEntity.setRegister_url(register_url);
            flag = true;
        } else challengeEntity.setRegister_url(challengeEntity.getRegister_url());

        if (flag == true) {
            challengeEntity.setUpdated_time(new Date());
        }
        challengeRepository.save(challengeEntity);
        logger.info("Update challenge successfully");
        return new Response<>(ServiceStatusCode.OK_STATUS, ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    @Transactional
    public Response<Void> deleteChallenge(Integer id) {
        logger.info("{}{}", DELETE_CHALLENGE, id);
        if (id == null) {
            logger.warn("{}{}", DELETE_CHALLENGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Challenge challengeEntity = challengeRepository.findChallengeById(id);
        if (challengeEntity == null) {
            logger.warn("{}{}", DELETE_CHALLENGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(ServiceStatusCode.NOT_FOUND_STATUS, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        challengeEntity.setStatus("unavailable");
        challengeRepository.save(challengeEntity);
        logger.info("Delete challenge successfully");
        return new Response<>(ServiceStatusCode.OK_STATUS, ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    @Transactional
    public Response<Void> restoreChallenge(Integer id) {
        logger.info("Restore challenge");
        if (id == null) {
        logger.warn("{}{}", RESTORE_CHALLENGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
        return new Response<>(ServiceStatusCode.BAD_REQUEST_STATUS, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Challenge challengeEntity =challengeRepository.restoreChallengeById(id);
        if (challengeEntity == null) {
            logger.warn("{}{}", DELETE_CHALLENGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(ServiceStatusCode.NOT_FOUND_STATUS, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        challengeEntity.setStatus("available");
        challengeRepository.save(challengeEntity);
        logger.info("Restore challenge successfully");
        return new Response<>(ServiceStatusCode.OK_STATUS, ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }
}
