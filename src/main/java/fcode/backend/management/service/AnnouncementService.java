package fcode.backend.management.service;

import fcode.backend.management.model.dto.AnnouncementDTO;
import fcode.backend.management.model.request.CreateAnnouncementRequest;
import fcode.backend.management.model.dto.NotificationDTO;
import fcode.backend.management.repository.entity.Member;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.AnnouncementRepository;
import fcode.backend.management.repository.MemberRepository;
import fcode.backend.management.repository.entity.Announcement;
import fcode.backend.management.service.constant.ServiceMessage;
import fcode.backend.management.service.constant.Status;
import fcode.backend.management.service.event.EventPublisher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnnouncementService {
    @Autowired
    AnnouncementRepository announcementRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    NotificationService notificationService;

    @Autowired
    EventPublisher eventPublisher;

    private static final Logger logger = LogManager.getLogger(AnnouncementService.class);
    private static final String CREATE_ANNOUNCEMENT = "Create announcement: ";
    private static final String UPDATE_ANNOUNCEMENT = "Update announcement: ";
    private static final String DELETE_ANNOUNCEMENT = "Delete announcement: ";
    private static final String GET_EMAIL_SET_OF_RECEIVERS = "Get email set of receivers: ";
    private static final String GET_NOTIFICATION_DTO = "Get NotificationDTO: ";
    private static final String INVALID_NOTIFICATION_RECEIVER_LIST = "Invalid notification receiver list";
    private static final String SEND_NOTIFICATION_AND_EMAIL_TO_MEMBERS_SUCCESSFULLY = "Sent notification and email to members successfully";
    private static final String START_ADDING_NOTIFICATION_TO_MEMBER_ENTITY = "Start adding notification to member entity";
    private static final String START_SENDING_NOTIFICATION_TO_RECEIVERS_VIA_WEBSOCKET = "Start sending notification to receivers via WebSocket";
    private static final String START_SENDING_NOTIFICATION_EMAIL_TO_RECEIVERS = "Start sending notification email to receivers";

    public Response<List<AnnouncementDTO>> getAllAnnouncements() {
        logger.info("getAllAnnouncements()");
        List<AnnouncementDTO> announcementDTOList = announcementRepository.getAllAnnouncements(Status.ACTIVE.toString()).stream()
                .map(announcementEntity -> modelMapper.map(announcementEntity, AnnouncementDTO.class)).collect(Collectors.toList());

        logger.info("Get all announcements success");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), announcementDTOList);
    }

    public Response<AnnouncementDTO> getAnnouncementById(Integer id) {
        logger.info("getAnnouncementById(announcementId: {})", id);
        Announcement announcement = announcementRepository.getByIdAndStatus(id, Status.ACTIVE.toString());

        if (announcement == null) {
            logger.warn("{}{}", "Get announcement by Id:", ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        AnnouncementDTO announcementDTO = modelMapper.map(announcement, AnnouncementDTO.class);

        logger.info("{}{}", "Get announcement by id: ", ServiceMessage.SUCCESS_MESSAGE.getMessage());
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), announcementDTO);
    }

    public Response<Set<NotificationDTO>> getNotificationsByMember(Integer userId) {
        logger.info("getNotificationsByMember(userId: {})", userId);

        Member member = memberRepository.findMemberById(userId);
        if (member == null) {
            logger.warn("{}{}", "Get notifications by Member:", ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        Set<NotificationDTO> notificationResponseSet = member.getNotificationSet().stream()
                .map(announcement -> modelMapper.map(announcement, NotificationDTO.class)).collect(Collectors.toSet());

        logger.info("get notifications by member success");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), notificationResponseSet);
    }

    public Response<List<AnnouncementDTO>> searchAnnouncements(String value) {
        logger.info("searchAnnouncements(title : {})", value);

        List<AnnouncementDTO> announcementDTOList = announcementRepository.searchAllByTitle("% %".replace(" ", value), Status.ACTIVE.toString()).stream()
                .map(announcementEntity -> modelMapper.map(announcementEntity, AnnouncementDTO.class)).collect(Collectors.toList());
        if (announcementDTOList.isEmpty()) {
            logger.warn("{}{}", "Search announcements by title:", ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        logger.info("Search announcements by title successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), announcementDTOList);
    }

    @Transactional
    public Response<Void> createAnnouncement(CreateAnnouncementRequest createAnnouncementRequest, Integer userId) {
        logger.info("createAnnouncement(createAnnouncementRequest: {})", createAnnouncementRequest);

        if (createAnnouncementRequest == null) {
            logger.warn("{}{}", CREATE_ANNOUNCEMENT, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }

        if (createAnnouncementRequest.getTitle() == null) {
            logger.warn("{}{}", CREATE_ANNOUNCEMENT, "Empty title");
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Empty title");
        }

        Set<String> emailSet = notificationService
                .getEmailListOfNotificationReceivers(createAnnouncementRequest.getInfoUserId(), createAnnouncementRequest.getInfoGroup());
        if (emailSet == null) {
            logger.warn("{}{}", CREATE_ANNOUNCEMENT, INVALID_NOTIFICATION_RECEIVER_LIST);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), INVALID_NOTIFICATION_RECEIVER_LIST);
        }
        logger.info("{}{}",GET_EMAIL_SET_OF_RECEIVERS, emailSet);

        Announcement announcement = modelMapper.map(createAnnouncementRequest, Announcement.class);
        announcement.setId(null);
        announcement.setStatus(Status.ACTIVE);
        announcement.setMember(memberRepository.getReferenceById(userId));
        announcement.setSendEmailWhenUpdate(false);
        Announcement announcementEntity = announcementRepository.save(announcement);
        logger.info("{}{}", CREATE_ANNOUNCEMENT, "Create new announcement success");

        NotificationDTO notificationDTO = modelMapper.map(announcementEntity, NotificationDTO.class);
        logger.info("{}{}", GET_NOTIFICATION_DTO, notificationDTO);

        logger.info("{}{}", CREATE_ANNOUNCEMENT, START_ADDING_NOTIFICATION_TO_MEMBER_ENTITY);
        notificationService.addNotificationToMember(announcementEntity, emailSet);

        logger.info("{}{}", CREATE_ANNOUNCEMENT, START_SENDING_NOTIFICATION_TO_RECEIVERS_VIA_WEBSOCKET);
        eventPublisher.sendNotificationTo(emailSet, notificationDTO);

        logger.info("{}{}", CREATE_ANNOUNCEMENT, START_SENDING_NOTIFICATION_EMAIL_TO_RECEIVERS);
        eventPublisher.sendEmailTo(emailSet, createAnnouncementRequest.getMailTitle(), createAnnouncementRequest.getMail());

        logger.info("{}{}", CREATE_ANNOUNCEMENT, SEND_NOTIFICATION_AND_EMAIL_TO_MEMBERS_SUCCESSFULLY);
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    @Transactional
    public Response<Void> updateAnnouncement(AnnouncementDTO announcementDto, Integer userId) {
        logger.info("updateAnnouncement(announcementDto:{})", announcementDto);

        if (announcementDto == null || announcementDto.getTitle() == null) {
            logger.warn("{}{}", UPDATE_ANNOUNCEMENT, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }

        if (announcementRepository.getByIdAndStatus(announcementDto.getId(), Status.ACTIVE.toString()) == null) {
            logger.warn("{}{}", UPDATE_ANNOUNCEMENT, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }

        Set<String> emailSet = notificationService
                .getEmailListOfNotificationReceivers(announcementDto.getInfoUserId(), announcementDto.getInfoGroup());
        if (emailSet == null) {
            logger.warn("{}{}", UPDATE_ANNOUNCEMENT, INVALID_NOTIFICATION_RECEIVER_LIST);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), INVALID_NOTIFICATION_RECEIVER_LIST);
        }
        logger.info("{}{}",GET_EMAIL_SET_OF_RECEIVERS, emailSet);

        Announcement announcement = modelMapper.map(announcementDto, Announcement.class);
        if (announcementDto.getSendEmailWhenUpdate() != null && announcementDto.getSendEmailWhenUpdate().booleanValue())
            announcement.setSendEmailWhenUpdate(true);
        announcement.setStatus(Status.ACTIVE);
        announcement.setMember(memberRepository.getReferenceById(userId));

        Announcement announcementEntity = announcementRepository.save(announcement);
        logger.info("{}{}", UPDATE_ANNOUNCEMENT, "Update announcement success");

        if (announcement.getSendEmailWhenUpdate().booleanValue()) {
            NotificationDTO notificationDTO = modelMapper.map(announcementEntity, NotificationDTO.class);
            logger.info("{}{}", GET_NOTIFICATION_DTO, notificationDTO);

            logger.info("{}{}", UPDATE_ANNOUNCEMENT, START_SENDING_NOTIFICATION_TO_RECEIVERS_VIA_WEBSOCKET);
            eventPublisher.sendNotificationTo(emailSet, notificationDTO);

            logger.info("{}{}", UPDATE_ANNOUNCEMENT, START_SENDING_NOTIFICATION_EMAIL_TO_RECEIVERS);
            eventPublisher.sendEmailTo(emailSet, announcementDto.getMailTitle(), announcementDto.getMail());
        }
        logger.info("{}{}", UPDATE_ANNOUNCEMENT, SEND_NOTIFICATION_AND_EMAIL_TO_MEMBERS_SUCCESSFULLY);
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    @Transactional
    public Response<Void> deleteAnnouncement(Integer id) {
        logger.info("deleteAnnouncement(announcementId: {})", id);

        Announcement announcement = announcementRepository.getByIdAndStatus(id, Status.ACTIVE.toString());
        if (announcement == null) {
            logger.warn("{}{}", DELETE_ANNOUNCEMENT, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }

        announcement.setStatus(Status.INACTIVE);
        Announcement announcementEntity = announcementRepository.save(announcement);

        Set<String> emailSet = notificationService
                .getEmailListOfNotificationReceivers(announcement.getInfoUserId(), announcement.getInfoGroup());
        logger.info("{}{}", GET_EMAIL_SET_OF_RECEIVERS, emailSet);

        if (emailSet != null) {
            notificationService.deleteNotificationFromMember(announcementEntity, emailSet);
            logger.info("{}{}", DELETE_ANNOUNCEMENT, "Removed notification from member");
        }

        logger.info("Delete announcement success");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }
}
