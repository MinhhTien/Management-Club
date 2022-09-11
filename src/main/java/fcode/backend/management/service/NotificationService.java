package fcode.backend.management.service;

import fcode.backend.management.model.dto.AnnouncementDTO;
import fcode.backend.management.model.dto.NotificationDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.NotificationRepository;
import fcode.backend.management.service.constant.ServiceMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    NotificationRepository notificationRepository;

    private static final Logger logger = LogManager.getLogger(NotificationService.class);
    private static final String CREATE_NOTIFICATION = "Create notification: ";
    private static final String UPDATE_NOTIFICATION = "Update notification: ";
    private static final String DELETE_NOTIFICATION = "Delete notification: ";

    @Transactional
    public Response<List<NotificationDTO>> getAllNotifications() {
        logger.info("getAnnoucements()");

        //       List<SubjectDTO> subjectDTOList ;//= subjectRepository.getAllSubjects().stream()
//                .map(subjectEntity -> modelMapper.map(subjectEntity, SubjectDTO.class)).collect(Collectors.toList());
//
//        logger.info("Get all subjects success");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<NotificationDTO> getNotificationById(Integer id) {
        logger.info("getNotificationById(notificationId: {})", id);

//        if(!subjectRepository.existsById(id)) {
//            logger.warn("{}{}", "Get subject by id:", ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
//            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
//        }
//        SubjectDTO subjectDTO = modelMapper.map(subjectRepository.findSubjectById(id), SubjectDTO.class);
//
//        logger.info("{}{}", "Get subject by id: ", ServiceMessage.SUCCESS_MESSAGE.getMessage());
        return  new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    @Transactional
    public Response<List<NotificationDTO>> searchNotifications(String value) {
        logger.info("searchNotifications(value : {})", value);

//        List<SubjectDTO> subjectDTOList = subjectRepository.searchAllByName("% %".replace(" ", value)).stream()
//                .map(subjectEntity -> modelMapper.map(subjectEntity, SubjectDTO.class)).collect(Collectors.toList());
//        if(subjectDTOList.isEmpty()) {
//            logger.warn("{}{}", "Search subject by name:", ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
//            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
//        }
//        logger.info("Search subject by name success");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    @Transactional
    public Response<Void> createNotification(NotificationDTO notificationDTO) {
        logger.info("createNotification(notificationDto: {})", notificationDTO);

//        if(subjectDto == null) {
//            logger.warn("{}{}",CREATE_SUBJECT, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
//            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
//        }
//        if(subjectRepository.findByName(subjectDto.getName()) != null) {
//            logger.warn("{}{}", CREATE_SUBJECT, "Subject already exist.");
//            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Subject already exist.");
//        }
//        Subject subject = modelMapper.map(subjectDto, Subject.class);
//        subjectRepository.save(subject);
//        logger.info("Create subject success");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> updateNotification(NotificationDTO notificationDto) {
        logger.info("updateNotification(notificationDto:{})", notificationDto);

//        if(subjectDto == null) {
//            logger.warn("{}{}", UPDATE_SUBJECT, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
//            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
//        }
//
//        if(!subjectRepository.existsById(subjectDto.getId())){
//            logger.warn("{}{}", UPDATE_SUBJECT, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
//            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
//        }
//
//        Subject oldSubject = subjectRepository.findByName(subjectDto.getName());
//        if(oldSubject != null && oldSubject.getSemester().equals(subjectDto.getSemester())) {
//            logger.warn("{}{}", UPDATE_SUBJECT, "Subject name already exist");
//            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Subject name already exist");
//        }
//        Subject subject = modelMapper.map(subjectDto, Subject.class);
//        subjectRepository.save(subject);
//        logger.info("Update subject success");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    @Transactional
    public Response<Void> deleteNotification(Integer id) {
        logger.info("deleteNotification(notificationId: {})", id);

//        if(!subjectRepository.existsById(id)) {
//            logger.warn("{}{}", DELETE_SUBJECT, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
//            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
//        }
//
//        if(resourceRepository.existsBySubject(id)) {
//            logger.warn("{}{}", DELETE_SUBJECT, "Some resources use this subject");
//            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Some resources use this subject");
//        }
//        Subject subject = subjectRepository.findSubjectById(id);
//        subjectRepository.delete(subject);
//        logger.info("Delete subject success");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }
}
