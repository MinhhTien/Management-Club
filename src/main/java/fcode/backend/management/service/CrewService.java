package fcode.backend.management.service;

import fcode.backend.management.model.dto.CrewDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.CrewAnnouncementRepository;
import fcode.backend.management.repository.CrewRepository;
import fcode.backend.management.repository.MemberRepository;
import fcode.backend.management.repository.entity.Crew;
import fcode.backend.management.repository.entity.Member;
import fcode.backend.management.service.constant.ServiceMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CrewService {
    @Autowired
    CrewRepository crewRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CrewAnnouncementRepository crewAnnouncementRepository;
    @Autowired
    ModelMapper modelMapper;

    public static final Logger logger = LogManager.getLogger(CrewService.class);
    public static final String CREATE_CREW_MESSAGE = "Create crew: ";
    public static final String GET_CREW_MESSAGE = "Get crew: ";
    public static final String UPDATE_CREW_MESSAGE = "Update crew: ";
    public static final String DELETE_CREW_MESSAGE = "Delete crew: ";
    public static final String ASSIGN_CREW_MESSAGE = "Assign member to crew: ";
    public Response<Void> createCrew(CrewDTO crewDTO) {
        logger.info("{}{}", CREATE_CREW_MESSAGE, crewDTO);
        if (crewDTO == null) {
            logger.warn("{}{}", CREATE_CREW_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Crew crew = modelMapper.map(crewDTO, Crew.class);
        crew.setId(null);
        logger.info("{}{}", CREATE_CREW_MESSAGE, crew);
        crewRepository.save(crew);
        logger.info("Create question successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<CrewDTO> getCrew(Integer id) {
        logger.info("{}{}", GET_CREW_MESSAGE, id);
        if (id == null) {
            logger.warn("{}{}", GET_CREW_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Crew crew = crewRepository.findCrewById(id);
        if (crew == null) {
            logger.warn("{}{}", GET_CREW_MESSAGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        CrewDTO crewDTO = modelMapper.map(crew, CrewDTO.class);
        logger.info("Get crew by Id successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), crewDTO);
    }

    public Response<Void> updateCrew(CrewDTO crewDTO) {
        logger.info("{}{}", UPDATE_CREW_MESSAGE, crewDTO);
        if (crewDTO == null) {
            logger.warn("{}{}", UPDATE_CREW_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Crew crew = crewRepository.findCrewById(crewDTO.getId());
        if (crew == null) {
            logger.warn("{}{}", UPDATE_CREW_MESSAGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        if (crewDTO.getName() != null) crew.setName(crewDTO.getName());
        if (crewDTO.getLevel() != null) crew.setLevel(crewDTO.getLevel());
        if (crewDTO.getMeetUrl() != null) crew.setMeetingUrl(crewDTO.getDriveUrl());
        if (crewDTO.getDriveUrl() != null) crew.setDriveUrl(crewDTO.getDriveUrl());
        crewRepository.save(crew);
        logger.info("Update crew successfully.");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> deleteCrew(Integer id) {
        logger.info("{}{}", DELETE_CREW_MESSAGE, id);
        if (id == null) {
            logger.warn("{}{}", DELETE_CREW_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Crew crew = crewRepository.findCrewById(id);
        if (crew == null) {
            logger.warn("{}{}", DELETE_CREW_MESSAGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        
        if (memberRepository.existsByCrew(new Crew(id))) {
            logger.warn("{}{}", DELETE_CREW_MESSAGE, "There are some members in this crew.");
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "There are some members in this crew.");
        }
        if (crewAnnouncementRepository.existsByCrew(new Crew(id))) {
            logger.warn("{}{}", DELETE_CREW_MESSAGE, "There are some announcements for this crew.");
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "There are some announcements in this crew.");
        }
        crewRepository.deleteById(crew.getId());
        logger.info("Delete crew successfully.");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> assignMemberToCrew(Integer memberId, Integer crewId) {
        logger.info("{}member Id: {} crew id: {}", ASSIGN_CREW_MESSAGE, memberId, crewId);
        if (memberId == null) {
            logger.warn("{}{}", ASSIGN_CREW_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if (crewId == null) {
            logger.warn("{}{}", ASSIGN_CREW_MESSAGE, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if (!memberRepository.existsById(memberId)) {
            logger.warn("{}{}", ASSIGN_CREW_MESSAGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        if (!crewRepository.existsById(crewId)) {
            logger.warn("{}{}", ASSIGN_CREW_MESSAGE, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        Member member = memberRepository.findMemberById(memberId);
        member.setCrew(crewRepository.findCrewById(crewId));
        memberRepository.save(member);
        logger.info("Assign member to a crew successfully.");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }



}
