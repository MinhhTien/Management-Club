package fcode.backend.management.service;

import fcode.backend.management.model.dto.AnnouncementDTO;
import fcode.backend.management.model.dto.EmailReceiverDTO;
import fcode.backend.management.model.request.CreateAnnouncementRequest;
import fcode.backend.management.model.dto.NotificationDTO;
import fcode.backend.management.repository.entity.Member;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.AnnouncementRepository;
import fcode.backend.management.repository.MemberRepository;
import fcode.backend.management.repository.entity.Announcement;
import fcode.backend.management.model.dto.EmailDetailDTO;
import fcode.backend.management.service.constant.ServiceMessage;
import fcode.backend.management.service.constant.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnnouncementService {
    @Autowired
    AnnouncementRepository announcementRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EmailService emailService;

    @Autowired
    NotificationService notificationService;

    private static final Logger logger = LogManager.getLogger(AnnouncementService.class);
    private static final String CREATE_ANNOUNCEMENT = "Create announcement: ";
    private static final String UPDATE_ANNOUNCEMENT = "Update announcement: ";
    private static final String DELETE_ANNOUNCEMENT = "Delete announcement: ";
    private static final String INVALID_NOTIFICATION_RECEIVER_LIST = "Invalid notification receiver list";

    public Response<List<AnnouncementDTO>> getAllAnnouncements() {
        logger.info("getAnnouncements()");

        List<AnnouncementDTO> announcementDTOList = announcementRepository.getAllAnnouncements(Status.ACTIVE.toString()).stream()
                .map(announcementEntity -> modelMapper.map(announcementEntity, AnnouncementDTO.class)).collect(Collectors.toList());

        logger.info("Get all announcements success");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), announcementDTOList);
    }

    public Response<AnnouncementDTO> getAnnouncementById(Integer id) {
        logger.info("getAnnouncementById(announcementId: {})", id);

        Announcement announcement = announcementRepository.getByIdAndStatus(id, Status.ACTIVE.toString());
        if (announcement == null) {
            logger.warn("{}{}", "Get announcement by id:", ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        AnnouncementDTO announcementDTO = modelMapper.map(announcement, AnnouncementDTO.class);

        logger.info("{}{}", "Get announcement by id: ", ServiceMessage.SUCCESS_MESSAGE.getMessage());
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), announcementDTO);
    }

    public Response<Set<NotificationDTO>> getNotificationsByMember(Integer userId) {
        logger.info("getNotificationsByMember(userId: {})", userId);

        Member member = memberRepository.findMemberById(userId);
        Set<NotificationDTO> notificationResponseSet = member.getNotificationSet().stream()
                .map(announcement -> modelMapper.map(announcement, NotificationDTO.class)).collect(Collectors.toSet());

        logger.info("get notifications by member success");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), notificationResponseSet);
    }

    public Response<List<AnnouncementDTO>> searchAnnouncements(String value) {
        logger.info("searchAnnouncements(value : {})", value);

        List<AnnouncementDTO> announcementDTOList = announcementRepository.searchAllByTitle("% %".replace(" ", value), Status.ACTIVE.toString()).stream()
                .map(announcementEntity -> modelMapper.map(announcementEntity, AnnouncementDTO.class)).collect(Collectors.toList());
        if (announcementDTOList.isEmpty()) {
            logger.warn("{}{}", "Search announcements by title:", ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        logger.info("Search announcements by title success");
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

        //Get email set of receivers
        Set<String> emailSet = notificationService
                .getEmailListOfNotificationReceivers(createAnnouncementRequest.getInfoUserId(), createAnnouncementRequest.getInfoGroup());
        if (emailSet == null) {
            logger.warn("{}{}", CREATE_ANNOUNCEMENT, INVALID_NOTIFICATION_RECEIVER_LIST);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), INVALID_NOTIFICATION_RECEIVER_LIST);
        }

        //Save announcement
        Announcement announcement = modelMapper.map(createAnnouncementRequest, Announcement.class);
        announcement.setId(null);
        announcement.setStatus(Status.ACTIVE);
        announcement.setMember(memberRepository.getReferenceById(userId));
        announcement.setSendEmailWhenUpdate(false);
        Announcement announcementEntity = announcementRepository.save(announcement);
        logger.info("Create announcement success");

        NotificationDTO notificationDTO = new NotificationDTO(
                announcementEntity.getId(), announcementEntity.getTitle(), announcementEntity.getDescription(),
                announcementEntity.getLocation(), announcementEntity.getImageUrl());
        //Add notification to members and send email.
        emailSet.forEach(email -> {
            notificationService.addNotificationToMember(announcementEntity, email);
            messagingTemplate.convertAndSendToUser(email, "/queue/private-messages", notificationDTO);
            EmailReceiverDTO emailReceiverDTO = memberRepository.getReceiverByEmail(email, Status.ACTIVE);
            emailService.sendHtmlEmail(new EmailDetailDTO(email, createAnnouncementRequest.getMailTitle(),
                    emailService.inputInfoToHtml(createAnnouncementRequest.getMail(), emailReceiverDTO.getStudentId(), emailReceiverDTO.getFirstName() + emailReceiverDTO.getLastName())));
        });
        //messagingTemplate.convertAndSend("/topic/messages", notificationDTO);
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
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }

        Announcement announcement = modelMapper.map(announcementDto, Announcement.class);

        //Send email with mail, mailTile, infoGroupId, infoUserId
        Set<String> emailSet = notificationService
                .getEmailListOfNotificationReceivers(announcementDto.getInfoUserId(), announcementDto.getInfoGroup());
        if (emailSet == null) {
            logger.warn("{}{}", UPDATE_ANNOUNCEMENT, INVALID_NOTIFICATION_RECEIVER_LIST);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), INVALID_NOTIFICATION_RECEIVER_LIST);
        }
        if (announcementDto.getSendEmailWhenUpdate() != null && announcementDto.getSendEmailWhenUpdate().booleanValue()) {
            announcement.setSendEmailWhenUpdate(true);
        } else announcement.setSendEmailWhenUpdate(false);

        announcement.setStatus(Status.ACTIVE);
        announcement.setMember(memberRepository.getReferenceById(userId));

        Announcement announcementEntity = announcementRepository.save(announcement);
        logger.info("Update announcement success");

        NotificationDTO notificationDTO = new NotificationDTO(
                announcementEntity.getId(), announcementEntity.getTitle(), announcementEntity.getDescription(),
                announcementEntity.getLocation(), announcementEntity.getImageUrl());
        emailSet.forEach(email -> {
            messagingTemplate.convertAndSendToUser(email, "/queue/private-messages", notificationDTO);
        });

        if(announcement.getSendEmailWhenUpdate()) {
            emailSet.forEach(email -> {
                EmailReceiverDTO emailReceiverDTO = memberRepository.getReceiverByEmail(email, Status.ACTIVE);
                emailService.sendHtmlEmail(new EmailDetailDTO(email, announcementDto.getMailTitle(),
                        emailService.inputInfoToHtml(announcementDto.getMail(), emailReceiverDTO.getStudentId(), emailReceiverDTO.getFirstName() + emailReceiverDTO.getLastName())));
            });
        }
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    @Transactional
    public Response<Void> deleteAnnouncement(Integer id) {
        logger.info("deleteAnnouncement(announcementId: {})", id);

        Announcement announcement = announcementRepository.getByIdAndStatus(id, Status.ACTIVE.toString());
        if (announcement == null) {
            logger.warn("{}{}", DELETE_ANNOUNCEMENT, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }

        announcement.setStatus(Status.INACTIVE);
        Announcement announcementEntity = announcementRepository.save(announcement);

        Set<String> emailSet = notificationService
                .getEmailListOfNotificationReceivers(announcement.getInfoUserId(), announcement.getInfoGroup());

        if (emailSet != null) {
            notificationService.deleteNotificationFromMember(announcementEntity, emailSet);
        }

        logger.info("Delete announcement success");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }
}
