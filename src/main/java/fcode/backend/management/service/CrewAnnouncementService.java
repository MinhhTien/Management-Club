package fcode.backend.management.service;

import fcode.backend.management.model.dto.CrewAnnouncementDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.CrewAnnouncementRepository;
import fcode.backend.management.repository.CrewRepository;
import fcode.backend.management.repository.entity.Crew;
import fcode.backend.management.repository.entity.CrewAnnouncement;
import fcode.backend.management.service.constant.ServiceMessage;
import fcode.backend.management.service.constant.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class CrewAnnouncementService {
    @Autowired
    CrewAnnouncementRepository crewAnnouncementRepository;
    @Autowired
    CrewRepository crewRepository;
    @Autowired
    ModelMapper modelMapper;

    public static final Logger logger = LogManager.getLogger(CrewAnnouncementService.class);
    public static final String CREATE_CREW_ANNOUNCEMENT_MESSAGE = "Create crew announcement: ";
    public static final String GET_CREW_ANNOUNCEMENT_MESSAGE = "Get crew announcement: ";
    public static final String UPDATE_CREW_ANNOUNCEMENT_MESSAGE = "Update crew announcement: ";
    public static final String DELETE_CREW_ANNOUNCEMENT_MESSAGE = "Delete crew announcement: ";
    public Response<Void> createCrewAnnouncement(CrewAnnouncementDTO crewAnnouncementDTO) {
        logger.info("{}{}", CREATE_CREW_ANNOUNCEMENT_MESSAGE, crewAnnouncementDTO);
        if (crewAnnouncementDTO == null) {
            logger.warn("{}{}", CREATE_CREW_ANNOUNCEMENT_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if (!crewRepository.existsById(crewAnnouncementDTO.getCrewId())) {
            logger.warn("{}{}", CREATE_CREW_ANNOUNCEMENT_MESSAGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        CrewAnnouncement crewAnnouncement = modelMapper.map(crewAnnouncementDTO, CrewAnnouncement.class);
        crewAnnouncementDTO.setId(null);
        crewAnnouncement.setStatus(Status.ACTIVE);
        crewAnnouncementRepository.save(crewAnnouncement);
        logger.info("{}{}", CREATE_CREW_ANNOUNCEMENT_MESSAGE, ServiceMessage.SUCCESS_MESSAGE);
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }






    @Transactional
    public Response<Set<CrewAnnouncementDTO>> getAllCrewAnnouncements() {
        logger.info("{}{}", GET_CREW_ANNOUNCEMENT_MESSAGE, "All crew announcements.");
        Set<CrewAnnouncementDTO> crewAnnouncementDTOSet = crewAnnouncementRepository.findCrewAnnouncementByStatus(Status.ACTIVE).stream().map(map -> modelMapper.map(map, CrewAnnouncementDTO.class)).collect(Collectors.toSet());
        logger.info("Get all crew announcements successfully.");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), crewAnnouncementDTOSet);
    }

    public Response<CrewAnnouncementDTO> getCrewAnnouncementById(Integer id) {
        logger.info("{}{}", GET_CREW_ANNOUNCEMENT_MESSAGE, id);
        if (id == null) {
            logger.warn("{}{}", GET_CREW_ANNOUNCEMENT_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        CrewAnnouncement crewAnnouncement = crewAnnouncementRepository.findCrewAnnouncementByIdAndStatus(id, Status.ACTIVE);
        if (crewAnnouncement == null) {
            logger.warn("{}{}", GET_CREW_ANNOUNCEMENT_MESSAGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        logger.info("Get crew announcement successfully.");
        CrewAnnouncementDTO crewAnnouncementDTO = modelMapper.map(crewAnnouncement, CrewAnnouncementDTO.class);
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), crewAnnouncementDTO);
    }


    public Response<Set<CrewAnnouncementDTO>> getAnnouncementsOfACrew(Integer crewId) {
        logger.info("{}{}", GET_CREW_ANNOUNCEMENT_MESSAGE, crewId);

        Crew crew = crewRepository.findCrewById(crewId);

        if (crew == null) {
            logger.warn("{}{}", GET_CREW_ANNOUNCEMENT_MESSAGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }

        var crewAnnouncements = crew.getCrewAnnouncements().stream().filter(crewAnnouncement -> crewAnnouncement.getStatus().equals(Status.ACTIVE)).collect(Collectors.toSet());
        var crewAnnouncementDTOSet = crewAnnouncements.stream().map(crewAnnouncement-> modelMapper.map(crewAnnouncement, CrewAnnouncementDTO.class)).collect(Collectors.toSet());
        logger.info("Get all announcement of a crew successfully.");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), crewAnnouncementDTOSet);
    }
    public Response<Void> updateCrewAnnouncement(CrewAnnouncementDTO crewAnnouncementDTO) {
        logger.info("{}{}", UPDATE_CREW_ANNOUNCEMENT_MESSAGE, crewAnnouncementDTO);
        if (crewAnnouncementDTO == null) {
            logger.warn("{}{}", UPDATE_CREW_ANNOUNCEMENT_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }

        CrewAnnouncement crewAnnouncement = crewAnnouncementRepository.findCrewAnnouncementByIdAndStatusIsNot(crewAnnouncementDTO.getId(), Status.INACTIVE);
        if (crewAnnouncement == null) {
            logger.warn("{}{}", UPDATE_CREW_ANNOUNCEMENT_MESSAGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        if (!crewRepository.existsById(crewAnnouncementDTO.getCrewId())) {
            logger.warn("{}{}", UPDATE_CREW_ANNOUNCEMENT_MESSAGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        if (crewAnnouncementDTO.getTitle() != null) crewAnnouncement.setTitle(crewAnnouncementDTO.getTitle());
        if (crewAnnouncementDTO.getDescription() != null) crewAnnouncement.setDescription(crewAnnouncementDTO.getDescription());
        if (crewAnnouncementDTO.getLocation() != null) crewAnnouncement.setLocation(crewAnnouncementDTO.getLocation());
        if (crewAnnouncementDTO.getImageUrl() != null) crewAnnouncement.setImageUrl(crewAnnouncementDTO.getImageUrl());
        if (crewAnnouncementDTO.getCrewId() != null) crewAnnouncement.setCrew(crewRepository.findCrewById(crewAnnouncementDTO.getCrewId()));
        crewAnnouncementRepository.save(crewAnnouncement);
        logger.info("Update crew announcement successfully.");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> deleteCrewAnnouncementById(Integer id) {
        logger.info("{}{}", DELETE_CREW_ANNOUNCEMENT_MESSAGE, id);
        CrewAnnouncement crewAnnouncement = crewAnnouncementRepository.findCrewAnnouncementByIdAndStatusIsNot(id, Status.INACTIVE);
        if (crewAnnouncement == null) {
            logger.warn("{}{}", DELETE_CREW_ANNOUNCEMENT_MESSAGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        crewAnnouncement.setStatus(Status.INACTIVE);
        crewAnnouncementRepository.save(crewAnnouncement);
        logger.info("Delete crew announcement successfully.");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }
}
