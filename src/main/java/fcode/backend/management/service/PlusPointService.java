package fcode.backend.management.service;

import fcode.backend.management.model.dto.PlusPointDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.AttendanceRepository;
import fcode.backend.management.repository.MemberRepository;
import fcode.backend.management.repository.PlusPointRepository;
import fcode.backend.management.repository.entity.Attendance;
import fcode.backend.management.repository.entity.Member;
import fcode.backend.management.repository.entity.PlusPoint;
import fcode.backend.management.service.constant.ServiceMessage;
import fcode.backend.management.service.constant.Status;
import fcode.backend.management.config.interceptor.State;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlusPointService {
    @Autowired
    PlusPointRepository plusPointRepository;

    @Autowired
    AttendanceRepository attendanceRepository;
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ModelMapper modelMapper;

    private static final Logger logger = LogManager.getLogger(PlusPointService.class);
    private static final String GET_PLUS_POINT = "Get plus point: ";
    private static final String CREATE_PLUS_POINT = "Create plus point: ";
    private static final String UPDATE_PLUS_POINT = "Update plus point: ";
    private static final String DELETE_PLUS_POINT = "Delete plus point: ";

    public Response<List<PlusPointDTO>> getAllPlusPoints() {
        logger.info("{}{}", GET_PLUS_POINT, "All plus point");
        List<PlusPointDTO> plusPointDTOList = plusPointRepository.getAllPlusPoints(Status.ACTIVE.toString()).stream()
                .map(plusPointEntity -> modelMapper.map(plusPointEntity, PlusPointDTO.class)).collect(Collectors.toList());
        logger.info("Get all plus points successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), plusPointDTOList);
    }

    public Response<PlusPointDTO> getPlusPointById(Integer id) {
        logger.info("{}{}", GET_PLUS_POINT, id);
        if (id == null) {
            logger.warn("{}{}", GET_PLUS_POINT, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        PlusPoint plusPoint = plusPointRepository.getByIdAndStatus(id, Status.ACTIVE.toString());
        if (plusPoint == null) {
            logger.warn("{}{}", GET_PLUS_POINT, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }

        PlusPointDTO plusPointDTO = modelMapper.map(plusPoint, PlusPointDTO.class);
        logger.info("{}{}{}", GET_PLUS_POINT, id, "successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), plusPointDTO);
    }

    public Response<List<PlusPointDTO>> getByMemberId(Integer memberId) {
        logger.info("{}{}", GET_PLUS_POINT, memberId);
        if (memberId == null) {
            logger.warn("{}{}", GET_PLUS_POINT, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }

        if (memberRepository.findMemberById(memberId) == null) {
            logger.warn("{}{}", GET_PLUS_POINT, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        List<PlusPointDTO> plusPointDTOList = plusPointRepository.getByMemberIdAndStatus(memberId, Status.ACTIVE.toString())
                .stream().map(plusPoint -> {
                    PlusPointDTO plusPointDTO = modelMapper.map(plusPoint, PlusPointDTO.class);
                    plusPointDTO.setMemberId(plusPoint.getMember().getId());
                    return plusPointDTO;
                }).collect(Collectors.toList());
        logger.info("Get plus point successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), plusPointDTOList);
    }

    public Response<List<PlusPointDTO>> getByMemberIdBetweenTime(Integer memberId, Date startDate, Date endDate) {
        logger.info("{}{}{}{}{}{}", GET_PLUS_POINT, memberId, "from", startDate, "to", endDate);
        if (memberId == null || startDate == null || endDate == null) {
            logger.warn("{}{}", GET_PLUS_POINT, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if (memberRepository.findMemberById(memberId) == null) {
            logger.warn("{}{}", GET_PLUS_POINT, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        List<PlusPointDTO> plusPointDTOList = plusPointRepository.getByMemberIdAndStatusBetweenTime(memberId, startDate, endDate, Status.ACTIVE.toString())
                .stream().map(plusPoint -> {
                    PlusPointDTO plusPointDTO = modelMapper.map(plusPoint, PlusPointDTO.class);
                    plusPointDTO.setMemberId(plusPoint.getMember().getId());
                    return plusPointDTO;
                }).collect(Collectors.toList());
        logger.info("Get plus point successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), plusPointDTOList);
    }

    public Response<PlusPointDTO> getTotalPointInPeriodTime(Integer memberId, Date startDate, Date endDate) {
        logger.info("{}{}{}{}{}{}", GET_PLUS_POINT, memberId, "from", startDate, "to", endDate);
        if (memberId == null || startDate == null || endDate == null) {
            logger.warn("{}{}", GET_PLUS_POINT, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if (memberRepository.findMemberById(memberId) == null) {
            logger.warn("{}{}", GET_PLUS_POINT, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        PlusPointDTO plusPointDTO = new PlusPointDTO();
        List<PlusPoint> plusPointList = plusPointRepository.getByMemberIdAndStatusBetweenTime(memberId, startDate, endDate, Status.ACTIVE.toString());
        List<Attendance> attendanceList = attendanceRepository.getByMemberIdBetweenTime(memberId, startDate, endDate);
        Integer totalPoint = 0;
        for (PlusPoint plusPoint : plusPointList) {
            totalPoint += plusPoint.getQuantity();
        }
        for (Attendance attendance : attendanceList) {
            if(attendance.getState().equals(State.ON_TIME)) {
                totalPoint += 5;
            } else totalPoint += 1;
        }
        plusPointDTO.setQuantity(totalPoint);
        plusPointDTO.setReason("Total point from "+ startDate + " to " + endDate + " of member ID: " + memberId);
        plusPointDTO.setMemberId(memberId);
        logger.info("{}{}", GET_PLUS_POINT, "successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), plusPointDTO);
    }

    public Response<Void> createPlusPoint(PlusPointDTO plusPointDTO) {
        logger.info("{}{}", CREATE_PLUS_POINT, plusPointDTO);
        if(plusPointDTO == null) {
            logger.warn("{}{}",CREATE_PLUS_POINT, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if(plusPointRepository.existsByMemberIdAndReason(plusPointDTO.getMemberId(), plusPointDTO.getReason()) != null) {
            logger.warn("{}{}", CREATE_PLUS_POINT, "Plus point is already existed");
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Plus point is already existed");
        }
        if(memberRepository.findMemberById(plusPointDTO.getMemberId()) == null) {
            logger.warn("{}{}", CREATE_PLUS_POINT, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        PlusPoint plusPoint = modelMapper.map(plusPointDTO, PlusPoint.class);
        plusPoint.setMember(new Member(plusPointDTO.getMemberId()));
        plusPoint.setStatus(fcode.backend.management.config.interceptor.Status.ACTIVE);
        plusPointRepository.save(plusPoint);
        logger.info("Create plus point successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> updatePlusPoint(PlusPointDTO plusPointDTO) {
        logger.info("{}{}", UPDATE_PLUS_POINT, plusPointDTO);
        if(plusPointDTO == null) {
            logger.warn("{}{}", UPDATE_PLUS_POINT, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        PlusPoint plusPoint = plusPointRepository.getByIdAndStatus(plusPointDTO.getId(), Status.ACTIVE.toString());
        if(plusPoint == null) {
            logger.warn("{}{}", UPDATE_PLUS_POINT, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        if(plusPointDTO.getId() != null && memberRepository.findMemberById(plusPointDTO.getMemberId()) != null) {
            plusPoint.setMember(new Member(plusPointDTO.getMemberId()));
            if(plusPointDTO.getQuantity() != null) {
                plusPoint.setQuantity(plusPointDTO.getQuantity());
            }
            if(plusPointDTO.getReason() != null) {
                plusPoint.setReason(plusPointDTO.getReason());
            }
            if(plusPointDTO.getDate() != null) {
                plusPoint.setDate(plusPointDTO.getDate());
            }
        } else {
            logger.warn("{}{}", UPDATE_PLUS_POINT, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        plusPointRepository.save(plusPoint);
        logger.info("Update plus point successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> deletePlusPoint(Integer id) {
        logger.info("{}{}", DELETE_PLUS_POINT, id);
        if(id == null) {
            logger.warn("{}{}", DELETE_PLUS_POINT, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        PlusPoint plusPoint = plusPointRepository.getByIdAndStatus(id, Status.ACTIVE.toString());
        if(plusPoint == null) {
            logger.warn("{}{}", DELETE_PLUS_POINT, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        plusPoint.setStatus(fcode.backend.management.config.interceptor.Status.INACTIVE);
        plusPointRepository.save(plusPoint);
        logger.info("Delete plus point successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }
}
