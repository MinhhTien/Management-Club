package fcode.backend.management.service;

import fcode.backend.management.model.dto.AnnouncementDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.AnnouncementRepository;
import fcode.backend.management.repository.MemberRepository;
import fcode.backend.management.repository.entity.Announcement;
import fcode.backend.management.service.constant.ServiceMessage;
import fcode.backend.management.service.constant.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnnouncementService {
    @Autowired
    AnnouncementRepository announcementRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ModelMapper modelMapper;

    private static final Logger logger = LogManager.getLogger(AnnouncementService.class);
    private static final String CREATE_ANNOUNCEMENT = "Create announcement: ";
    private static final String UPDATE_ANNOUNCEMENT = "Update announcement: ";
    private static final String DELETE_ANNOUNCEMENT = "Delete announcement: ";

    public Response<List<AnnouncementDTO>> getAllAnnoucements() {
        logger.info("getAnnoucements()");

               List<AnnouncementDTO> announcementDTOList = announcementRepository.getAllAnnouncements(Status.ACTIVE).stream()
                .map(announcementEntity -> modelMapper.map(announcementEntity, AnnouncementDTO.class)).collect(Collectors.toList());

        logger.info("Get all annoucements success");
       return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), announcementDTOList);
    }

    public Response<AnnouncementDTO> getAnnouncementById(Integer id) {
        logger.info("getAnnouncementById(announcementId: {})", id);

        Announcement announcement = announcementRepository.getByIdAndStatus(id, Status.ACTIVE);
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

        List<AnnouncementDTO> announcementDTOList = announcementRepository.searchAllByTitle("% %".replace(" ", value), Status.ACTIVE).stream()
                .map(subjectEntity -> modelMapper.map(subjectEntity, AnnouncementDTO.class)).collect(Collectors.toList());
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
            logger.warn("{}{}",CREATE_ANNOUNCEMENT, "Empty title");
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Empty title");
        }

        Announcement announcement = modelMapper.map(announcementDto, Announcement.class);
        announcement.setId(null);
        announcement.setStatus(Status.ACTIVE);
        announcement.setMember(memberRepository.getReferenceById(userId));
        announcementRepository.save(announcement);
        logger.info("Create announcement success");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    @Transactional
    public Response<Void> updateAnnouncement(AnnouncementDTO announcementDto) {
        logger.info("updateAnnouncement(announcementDto:{})", announcementDto);

        if(announcementDto == null) {
            logger.warn("{}{}", UPDATE_ANNOUNCEMENT, ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }

        Announcement announcement = announcementRepository.getByIdAndStatus(announcementDto.getId(), Status.ACTIVE);
        if(announcement == null){
            logger.warn("{}{}", UPDATE_ANNOUNCEMENT, ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }

        if(announcementDto.getTitle()!=null) announcement.setTitle(announcementDto.getTitle());
        if(announcementDto.getDescription()!=null) announcement.setDescription(announcementDto.getDescription());
        if(announcementDto.getInfoGroup()!=null) announcement.setInfoGroup(announcementDto.getInfoGroup());
        if(announcementDto.getInfoUserId()!=null) announcement.setInfoUserId(announcementDto.getInfoUserId());
        if(announcementDto.getLocation()!=null) announcement.setLocation(announcementDto.getLocation());
        if(announcementDto.getImageUrl()!=null) announcement.setImageUrl(announcementDto.getImageUrl());
        if(announcementDto.getMail()!=null) announcement.setMail(announcementDto.getMail());
        if(announcementDto.getMailTitle()!=null) announcement.setMailTitle(announcementDto.getMailTitle());

        if(announcementDto.getSendEmailWhenUpdate()==true) {
            announcement.setSendEmailWhenUpdate(announcementDto.getSendEmailWhenUpdate());
            //Send email with mail, mailTile, infoGroupId, infoUserId
        }

        announcementRepository.save(announcement);
        logger.info("Update announcement success");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    @Transactional
    public Response<Void> deleteAnnouncement(Integer id) {
        logger.info("deleteAnnouncement(announcementId: {})", id);

        Announcement announcement = announcementRepository.getByIdAndStatus(id, Status.ACTIVE);
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
