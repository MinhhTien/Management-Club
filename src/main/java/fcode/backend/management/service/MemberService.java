package fcode.backend.management.service;

import fcode.backend.management.config.Role;
import fcode.backend.management.service.constant.Status;
import fcode.backend.management.model.dto.MemberDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.MemberRepository;
import fcode.backend.management.repository.entity.Member;
import fcode.backend.management.service.constant.ServiceMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ModelMapper modelMapper;

    private static final Logger logger = LogManager.getLogger(MemberService.class);
    private static final String CREATE_MEMBER = "Create member: ";
    private static final String UPDATE_MEMBER = "Update member: ";
    private static final String DELETE_MEMBER = "Delete member: ";
    private static final String GET_MEMBER_ID = "Get member id: ";

    public Response<List<MemberDTO>> getAllMembers() {
        logger.info("Get all members");
        List<MemberDTO> memberDTOList = memberRepository.findALlMember(fcode.backend.management.service.constant.Status.ACTIVE.toString()).stream()
                .map(member -> modelMapper.map(member, MemberDTO.class)).collect(Collectors.toList());
        logger.info("Get all members successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), memberDTOList);
    }

    public Response<MemberDTO> getMemberById(Integer id) {
        logger.info("Get member by ID: {}", id);
        if (id == null) {
            logger.warn("{}{}", GET_MEMBER_ID, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if(memberRepository.findMemberById(id) == null) {
            logger.warn("{}{}", GET_MEMBER_ID, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        Member member = memberRepository.findMemberById(id);
        MemberDTO memberDTO = modelMapper.map(member, MemberDTO.class);
        logger.info("{}{}", GET_MEMBER_ID, ServiceMessage.SUCCESS_MESSAGE);
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), memberDTO);
    }

    public Response<MemberDTO> getMemberByStudentId(String studentId) {
        logger.info("Get member by student id: {}", studentId);
        if(studentId == null) {
            logger.warn("{}{}", "Get member by student id: ", ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if(memberRepository.findMemberByStudentId(studentId, fcode.backend.management.service.constant.Status.ACTIVE.toString()) == null) {
            logger.warn("{}{}", "Get member by student id: ", ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        Member member = memberRepository.findMemberByStudentId(studentId, Status.ACTIVE.toString());
        MemberDTO memberDTO = modelMapper.map(member, MemberDTO.class);
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), memberDTO);
    }

    public Response<List<MemberDTO>> getMemberByLastname (String lastname) {
        logger.info("Get member by student lastname: {}", lastname);
        if(lastname == null) {
            logger.warn("{}{}", "Get member by student lastname: ", ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if(memberRepository.findMemberByLastname(lastname, Status.ACTIVE.toString()) == null) {
            logger.warn("{}{}", "Get member by student lastname: ", ServiceMessage.ID_NOT_EXIST_MESSAGE);
        }
        List<MemberDTO> memberDTOList = memberRepository.findMemberByLastname(lastname, Status.ACTIVE.toString()).stream()
                .map(member -> modelMapper.map(member, MemberDTO.class)).collect(Collectors.toList());
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), memberDTOList);
    }

    @Transactional
    public Response<Void> createMember(MemberDTO memberDTO) {
        logger.info("{}{}", CREATE_MEMBER, memberDTO);
        if(memberDTO == null) {
            logger.warn("{}{}", CREATE_MEMBER, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if(memberRepository.findMemberByStudentId(memberDTO.getStudentId(), Status.ACTIVE.toString()) != null) {
            logger.warn("{}{}", CREATE_MEMBER, "Student code is already exist");
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Member is already exist");
        }

        Member member = modelMapper.map(memberDTO, Member.class);
        member.setStatus(Status.ACTIVE);
        logger.info("{}{}", CREATE_MEMBER, member);
        memberRepository.save(member);
        logger.info("Create member successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> updateMember(MemberDTO memberDTO) {
        logger.info("{}{}", UPDATE_MEMBER, memberDTO);
        if(memberDTO == null) {
            logger.warn("{}{}", UPDATE_MEMBER, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if(memberRepository.findMemberById(memberDTO.getId()) == null) {
            logger.warn("{}{}", UPDATE_MEMBER, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }

        Member memberEntity = modelMapper.map(memberDTO, Member.class);
        memberRepository.save(memberEntity);
        logger.info("Update member successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> deleteMember(Integer id) {
        logger.info("{}{}", DELETE_MEMBER, id);
        if(id == null) {
            logger.warn("{}{}", DELETE_MEMBER, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Member memberEntity = memberRepository.findMemberById(id);
        if(memberEntity == null) {
            logger.warn("{}{}", DELETE_MEMBER, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        memberEntity.setStatus(Status.INACTIVE);
        memberRepository.save(memberEntity);
        logger.info("Delete member successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }
}
