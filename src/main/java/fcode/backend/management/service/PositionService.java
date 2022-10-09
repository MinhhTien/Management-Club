package fcode.backend.management.service;

import fcode.backend.management.model.dto.PositionDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.MemberRepository;
import fcode.backend.management.repository.PositionRepository;
import fcode.backend.management.repository.entity.Member;
import fcode.backend.management.repository.entity.Position;
import fcode.backend.management.service.constant.ServiceMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PositionService {
    @Autowired
    PositionRepository positionRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ModelMapper modelMapper;

    private static final Logger logger = LogManager.getLogger(PositionService.class);
    private static final String GET_POSITION = "Get position: ";
    private static final String CREATE_POSITION = "Create position: ";
    private static final String UPDATE_POSITION = "Update position: ";
    private static final String DELETE_POSITION = "Delete position: ";
    private static final String ASSIGN_POSITION = "Assign to position: ";

    public Response<List<PositionDTO>> getAllPositions() {
        logger.info("Get all position");
        List<PositionDTO> positionDTOList = positionRepository.getAllPosition().stream()
                .map(position -> modelMapper.map(position, PositionDTO.class)).collect(Collectors.toList());
        logger.info("Get all positions successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), positionDTOList);
    }

    public Response<PositionDTO> getPositionById(Integer id) {
        logger.info("{}{}", GET_POSITION, id);
        if (id == null) {
            logger.warn("{}{}", GET_POSITION, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Position position = positionRepository.getById(id);
        if(position == null) {
            logger.warn("{}{}", GET_POSITION, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        PositionDTO positionDTO = modelMapper.map(position, PositionDTO.class);
        logger.info("{}{}{}", GET_POSITION, id, "successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), positionDTO);
    }

    public Response<Void> createPosition(PositionDTO positionDTO) {
        logger.info("{}{}", CREATE_POSITION, positionDTO);
        if (positionDTO == null) {
            logger.warn("{}{}", CREATE_POSITION, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Position position = modelMapper.map(positionDTO, Position.class);
        position.setId(null);
        logger.info("{}{}", CREATE_POSITION, positionDTO);
        positionRepository.save(position);
        logger.info("Create position successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> updatePosition(PositionDTO positionDTO) {
        logger.info("{}{}", UPDATE_POSITION, positionDTO);
        if (positionDTO == null) {
            logger.warn("{}{}", UPDATE_POSITION, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Position position = positionRepository.getById(positionDTO.getId());
        if (position == null) {
            logger.warn("{}{}", UPDATE_POSITION, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        if (positionDTO.getPosition() != null) position.setPosition(positionDTO.getPosition());
        positionRepository.save(position);
        logger.info("Update position successfully.");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> deletePosition(Integer id) {
        logger.info("{}{}", DELETE_POSITION, id);
        if (id == null) {
            logger.warn("{}{}", DELETE_POSITION, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Position position = positionRepository.getById(id);
        if(position == null) {
            logger.warn("{}{}", DELETE_POSITION, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        if(memberRepository.existsByPosition(new Position(id))) {
            logger.warn("{}{}", DELETE_POSITION, "There are some member in this position.");
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "There are some member in this position.");
        }
        positionRepository.deleteById(position.getId());
        logger.info("Delete position successfully.");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> assignMemberToPosition(Integer memberId, Integer positionId) {
        logger.info("{}member Id: {} position id: {}", ASSIGN_POSITION, memberId, positionId);

        if (memberId == null) {
            logger.warn("{}{}", ASSIGN_POSITION, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if (positionId == null) {
            logger.warn("{}{}", ASSIGN_POSITION, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if (!memberRepository.existsById(memberId)) {
            logger.warn("{}{}", ASSIGN_POSITION, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        if(!positionRepository.existsById(positionId)) {
            logger.warn("{}{}", ASSIGN_POSITION, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        Member member = memberRepository.findMemberById(memberId);
        member.setPosition(positionRepository.getById(positionId));
        memberRepository.save(member);
        logger.info("Assign member to position successfully.");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }
}
