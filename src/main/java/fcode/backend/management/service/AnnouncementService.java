package fcode.backend.management.service;

import fcode.backend.management.model.dto.AnnouncementDTO;
import fcode.backend.management.model.dto.EmailReceiverDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.AnnouncementRepository;
import fcode.backend.management.repository.MemberRepository;
import fcode.backend.management.repository.entity.Announcement;
import fcode.backend.management.model.dto.EmailDetailDTO;
import fcode.backend.management.service.constant.ServiceMessage;
import fcode.backend.management.service.constant.Status;
import org.apache.commons.validator.GenericValidator;
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
    EmailService emailService;

    @Autowired
    GenericValidator genericValidator;

    private static final Logger logger = LogManager.getLogger(AnnouncementService.class);
    private static final String CREATE_ANNOUNCEMENT = "Create announcement: ";
    private static final String UPDATE_ANNOUNCEMENT = "Update announcement: ";
    private static final String DELETE_ANNOUNCEMENT = "Delete announcement: ";
    private static final String INVALID_EMAIL_RECEIVER_LIST = "Invalid email receiver list";
    private static final String INVALID_EMAIL_GROUP_RECEIVER_LIST = "Invalid email group receiver list";

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
        if(announcement == null) {
            logger.warn("{}{}", "Get announcement by id:", ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        AnnouncementDTO announcementDTO = modelMapper.map(announcement, AnnouncementDTO.class);

        logger.info("{}{}", "Get announcement by id: ", ServiceMessage.SUCCESS_MESSAGE.getMessage());
        return  new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), announcementDTO);
    }

    public Response<List<AnnouncementDTO>> searchAnnouncements(String value) {
        logger.info("searchAnnouncements(value : {})", value);

        List<AnnouncementDTO> announcementDTOList = announcementRepository.searchAllByTitle("% %".replace(" ", value), Status.ACTIVE.toString()).stream()
                .map(announcementEntity -> modelMapper.map(announcementEntity, AnnouncementDTO.class)).collect(Collectors.toList());
        if(announcementDTOList.isEmpty()) {
            logger.warn("{}{}", "Search announcements by title:", ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        logger.info("Search announcements by title success");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(),announcementDTOList);
    }

    @Transactional
    public Response<Void> createAnnouncement(AnnouncementDTO announcementDto, Integer userId) {
        logger.info("createAnnouncement(announcementDto: {})", announcementDto);

        if(announcementDto == null) {
            logger.warn("{}{}",CREATE_ANNOUNCEMENT, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }

        if(announcementDto.getTitle() == null) {
            logger.warn("{}{}", CREATE_ANNOUNCEMENT, "Empty title");
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Empty title");
        }

        Set<String> emailSet = new HashSet<>();
        if(!genericValidator.isBlankOrNull(announcementDto.getInfoUserId())) {
            List<Integer> userIdList = emailService.parseValidInfoText(announcementDto.getInfoUserId(), "&");
            if (userIdList == null) {
                logger.warn("{}{}", CREATE_ANNOUNCEMENT, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
                return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            }

            emailSet = emailService.parseInfoUserIdToEmail(userIdList);
            if (emailSet == null) {
                logger.warn("{}{}", CREATE_ANNOUNCEMENT, INVALID_EMAIL_RECEIVER_LIST);
                return new Response<>(HttpStatus.BAD_REQUEST.value(), INVALID_EMAIL_RECEIVER_LIST);
            }
        }

        Set<String> emailGroupSet = new HashSet<>();
        if(!genericValidator.isBlankOrNull(announcementDto.getInfoGroup())) {
        Map<String, List<Integer>> groupConditionMap = emailService.parseValidInfoGroup(announcementDto.getInfoGroup());
        if(groupConditionMap==null) {
            logger.warn("{}{}", CREATE_ANNOUNCEMENT, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }

        emailGroupSet = emailService.parseInfoGroupToEmail(groupConditionMap);
        if(emailGroupSet == null) {
            logger.warn("{}{}", CREATE_ANNOUNCEMENT, INVALID_EMAIL_GROUP_RECEIVER_LIST);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), INVALID_EMAIL_GROUP_RECEIVER_LIST);
        }
        }
        emailSet.addAll(emailGroupSet);

        Announcement announcement = modelMapper.map(announcementDto, Announcement.class);
        announcement.setId(null);
        announcement.setStatus(Status.ACTIVE);
        announcement.setMember(memberRepository.getReferenceById(userId));
        announcement.setSendEmailWhenUpdate(false);
        announcementRepository.save(announcement);
        logger.info("Create announcement success");

        emailSet.forEach(email -> {
            EmailReceiverDTO emailReceiverDTO = memberRepository.getReceiverByEmail(email, Status.ACTIVE);
            emailService.sendHtmlEmail(new EmailDetailDTO(email, announcementDto.getMailTitle(),
                    emailService.inputInfoToHtml(announcementDto.getMail(), emailReceiverDTO.getStudentId(), emailReceiverDTO.getFirstName() + emailReceiverDTO.getLastName())));
        });

        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    @Transactional
    public Response<Void> updateAnnouncement(AnnouncementDTO announcementDto, Integer userId) {
        logger.info("updateAnnouncement(announcementDto:{})", announcementDto);

        if(announcementDto == null || announcementDto.getTitle() == null) {
            logger.warn("{}{}", UPDATE_ANNOUNCEMENT, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }

        if(announcementRepository.getByIdAndStatus(announcementDto.getId(), Status.ACTIVE.toString()) == null){
            logger.warn("{}{}", UPDATE_ANNOUNCEMENT, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }

        Announcement announcement = modelMapper.map(announcementDto, Announcement.class);
        Set<String> emailSet = new HashSet<>();
        if(announcementDto.getSendEmailWhenUpdate() != null  && announcementDto.getSendEmailWhenUpdate().booleanValue()) {
            announcement.setSendEmailWhenUpdate(true);
            //Send email with mail, mailTile, infoGroupId, infoUserId
            if(!genericValidator.isBlankOrNull(announcementDto.getInfoUserId())) {
                List<Integer> userIdList = emailService.parseValidInfoText(announcementDto.getInfoUserId(), "&");
                if (userIdList == null) {
                    logger.warn("{}{}", UPDATE_ANNOUNCEMENT, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
                    return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
                }

                emailSet = emailService.parseInfoUserIdToEmail(userIdList);
                if (emailSet == null) {
                    logger.warn("{}{}", UPDATE_ANNOUNCEMENT, INVALID_EMAIL_RECEIVER_LIST);
                    return new Response<>(HttpStatus.BAD_REQUEST.value(), INVALID_EMAIL_RECEIVER_LIST);
                }
            }

            Set<String> emailGroupSet = new HashSet<>();
            if(!genericValidator.isBlankOrNull(announcementDto.getInfoGroup())) {
                Map<String, List<Integer>> groupConditionMap = emailService.parseValidInfoGroup(announcementDto.getInfoGroup());
                if(groupConditionMap==null) {
                    logger.warn("{}{}", UPDATE_ANNOUNCEMENT, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
                    return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
                }

                emailGroupSet = emailService.parseInfoGroupToEmail(groupConditionMap);
                if(emailGroupSet == null) {
                    logger.warn("{}{}", UPDATE_ANNOUNCEMENT, INVALID_EMAIL_GROUP_RECEIVER_LIST);
                    return new Response<>(HttpStatus.BAD_REQUEST.value(), INVALID_EMAIL_GROUP_RECEIVER_LIST);
                }
            }
            emailSet.addAll(emailGroupSet);
        } else announcement.setSendEmailWhenUpdate(false);

        announcement.setStatus(Status.ACTIVE);
        announcement.setMember(memberRepository.getReferenceById(userId));

        announcementRepository.save(announcement);
        logger.info("Update announcement success");
        emailSet.forEach(email -> {
            EmailReceiverDTO emailReceiverDTO = memberRepository.getReceiverByEmail(email, Status.ACTIVE);
            emailService.sendHtmlEmail(new EmailDetailDTO(email, announcementDto.getMailTitle(),
                    emailService.inputInfoToHtml(announcementDto.getMail(), emailReceiverDTO.getStudentId(), emailReceiverDTO.getFirstName() + emailReceiverDTO.getLastName())));
        });
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    @Transactional
    public Response<Void> deleteAnnouncement(Integer id) {
        logger.info("deleteAnnouncement(announcementId: {})", id);

        Announcement announcement = announcementRepository.getByIdAndStatus(id, Status.ACTIVE.toString());
        if(announcement == null) {
            logger.warn("{}{}", DELETE_ANNOUNCEMENT, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }

        announcement.setStatus(Status.INACTIVE);
        announcementRepository.save(announcement);
        logger.info("Delete announcement success");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }
}
