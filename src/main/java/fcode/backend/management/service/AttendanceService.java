package fcode.backend.management.service;

import fcode.backend.management.model.dto.AttendanceDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.AttendanceRepository;
import fcode.backend.management.repository.EventRepository;
import fcode.backend.management.repository.MemberRepository;
import fcode.backend.management.repository.entity.Attendance;
import fcode.backend.management.repository.entity.Event;
import fcode.backend.management.repository.entity.Member;
import fcode.backend.management.service.constant.ServiceMessage;
import fcode.backend.management.service.constant.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {
    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ModelMapper modelMapper;

    private static final Logger logger = LogManager.getLogger(AttendanceService.class);
    private static final String GET_ATTENDANCE = "Get attendance: ";
    private static final String CREATE_ATTENDANCE = "Create attendance: ";
    private static final String UPDATE_ATTENDANCE = "Update attendance: ";
    private static final String DELETE_ATTENDANCE = "Delete attendance: ";

    public Response<List<AttendanceDTO>> getAllAttendances() {
        logger.info("Get all attendance");
        List<AttendanceDTO> attendanceDTOList = attendanceRepository.findAllAttendances().stream()
                .map(attendance -> {
                    AttendanceDTO attendanceDTO = modelMapper.map(attendance, AttendanceDTO.class);
                    attendanceDTO.setMemberId(attendance.getMember().getId());
                    attendanceDTO.setStudentId(attendance.getMember().getStudentId());
                    attendanceDTO.setLastName(attendance.getMember().getLastName());
                    attendanceDTO.setEventId(attendance.getEvent().getId());
                    return attendanceDTO;
                }).collect(Collectors.toList());

        logger.info("Get all attendances successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), attendanceDTOList);
    }

    public Response<List<AttendanceDTO>> getAttendancesByEventId(Integer eventId) {
        logger.info("Get attendance by event id(Event id: {})", eventId);
        if(eventId == null) {
            logger.warn("{}{}", "Get attendance by event id:", ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if(eventRepository.findEventById(eventId, fcode.backend.management.config.interceptor.Status.ACTIVE.toString()) == null) {
            logger.warn("{}{}", "Get attendance by event id: ", ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        List<AttendanceDTO> attendanceDTOList = attendanceRepository.getAttendancesByEventId(eventId)
                .stream().map(attendance -> {
                    AttendanceDTO attendanceDTO = modelMapper.map(attendance, AttendanceDTO.class);
                    attendanceDTO.setStudentId(attendance.getMember().getStudentId());
                    attendanceDTO.setLastName(attendance.getMember().getLastName());
                    return attendanceDTO;
                }).collect(Collectors.toList());
        logger.info("{}{}", GET_ATTENDANCE, "successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), attendanceDTOList);
    }
    public Response<List<AttendanceDTO>> getAttendancesByMemberId(Integer memberId) {
        logger.info("Get attendance by member id(Member id: {})", memberId);
        if(memberId == null) {
            logger.warn("{}{}", "Get attendance by member id:", ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if(memberRepository.findMemberById(memberId) == null) {
            logger.warn("{}{}", "Get attendance by event id: ", ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        List<AttendanceDTO> attendanceDTOList = attendanceRepository.getAttendancesByMemberId(memberId)
                .stream().map(attendance -> {
                    AttendanceDTO attendanceDTO = modelMapper.map(attendance, AttendanceDTO.class);
                    attendanceDTO.setStudentId(attendance.getMember().getStudentId());
                    attendanceDTO.setLastName(attendance.getMember().getLastName());
                    return attendanceDTO;
                }).collect(Collectors.toList());
        logger.info("Get attendances successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), attendanceDTOList);
    }

    public Response<List<AttendanceDTO>> getAttendancesByStudentID(String studentId) {
        logger.info("Get attendance by Student id(Student id: {})", studentId);
        if(studentId == null) {
            logger.warn("{}{}", "Get attendance by student id:", ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if(memberRepository.findMemberByStudentId(studentId, Status.ACTIVE.toString()) == null) {
            logger.warn("{}{}", "Get attendance by student id: ", ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        List<AttendanceDTO> attendanceDTOList = attendanceRepository.getAttendancesByStudentId(studentId)
                .stream().map(attendance -> {
                    AttendanceDTO attendanceDTO = modelMapper.map(attendance, AttendanceDTO.class);
                    attendanceDTO.setStudentId(attendance.getMember().getStudentId());
                    attendanceDTO.setLastName(attendance.getMember().getLastName());
                    return attendanceDTO;
                }).collect(Collectors.toList());
        logger.info("Get attendances successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), attendanceDTOList);
    }

    public Response<Void> createAttendance(AttendanceDTO attendanceDTO) {
        logger.info("{}{}", CREATE_ATTENDANCE, attendanceDTO);
        if(attendanceDTO == null) {
            logger.warn("{}{}",CREATE_ATTENDANCE, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if(attendanceRepository.existsByMemberIdAndEventId(attendanceDTO.getMemberId(), attendanceDTO.getEventId()) != null) {
            logger.warn("{}{}", CREATE_ATTENDANCE, "Attendance is already existed");
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Attendance is already existed");
        }
        if(memberRepository.findMemberById(attendanceDTO.getMemberId()) == null || eventRepository.findEventById(attendanceDTO.getEventId(), fcode.backend.management.config.interceptor.Status.ACTIVE.toString()) == null) {
            logger.warn("{}{}", CREATE_ATTENDANCE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        Attendance attendance = modelMapper.map(attendanceDTO, Attendance.class);
        attendance.setMember(new Member(attendanceDTO.getMemberId()));
        attendance.setEvent(new Event(attendanceDTO.getEventId()));
        attendance.setState(attendanceDTO.getState());
        attendanceRepository.save(attendance);
        logger.info("Create attendance successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> updateAttendance(AttendanceDTO attendanceDTO) {
        logger.info("{}{}", UPDATE_ATTENDANCE, attendanceDTO);
        if(attendanceDTO == null) {
            logger.warn("{}{}", UPDATE_ATTENDANCE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Attendance attendanceEntity = attendanceRepository.getAttendanceById(attendanceDTO.getId());
        if(attendanceEntity == null) {
            logger.warn("{}{}", UPDATE_ATTENDANCE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        if(attendanceDTO.getDate() != null) {
            attendanceEntity.setDate(attendanceDTO.getDate());
        } else attendanceEntity.setDate(attendanceEntity.getDate());
        if(attendanceDTO.getState() != null) {
            attendanceEntity.setState(attendanceDTO.getState());
        } else attendanceEntity.setState(attendanceEntity.getState());

        attendanceRepository.save(attendanceEntity);
        logger.info("Update attendance successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> deleteAttendance(Integer id) {
        logger.info("{}{}", DELETE_ATTENDANCE, id);
        if(id == null) {
            logger.warn("{}{}", DELETE_ATTENDANCE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Attendance attendance = attendanceRepository.getAttendanceById(id);
        if(attendance == null) {
            logger.warn("{}{}", DELETE_ATTENDANCE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        attendanceRepository.delete(attendance);
        logger.info("Delete attendance successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }
}
