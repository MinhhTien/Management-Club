package fcode.backend.management.service;


import fcode.backend.management.config.interceptor.Regex;
import fcode.backend.management.repository.CrewRepository;
import fcode.backend.management.repository.PositionRepository;
import fcode.backend.management.repository.entity.Crew;
import fcode.backend.management.repository.entity.Position;
import fcode.backend.management.service.constant.Status;
import fcode.backend.management.model.dto.MemberDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.MemberRepository;
import fcode.backend.management.repository.entity.Member;
import fcode.backend.management.service.constant.ServiceMessage;
import net.bytebuddy.utility.RandomString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class MemberService {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PositionRepository positionRepository;

    @Autowired
    CrewRepository crewRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    JavaMailSender javaMailSender;

    @Value("${student.email.domain}")
    private String studentEmailDomain;

    @Value("${spring.mail.username}")
    private String sender;

    private static final Logger logger = LogManager.getLogger(MemberService.class);
    private static final String GET_MEMBER = "Get member: ";
    private static final String UPDATE_MEMBER = "Update member: ";
    private static final String DELETE_MEMBER = "Delete member: ";

    Pattern studentIdPattern = Pattern.compile(Regex.STUDENT_ID_PATTERN);
    Pattern mailPattern = Pattern.compile(Regex.MAIL_PATTERN);

    public Response<List<MemberDTO>> getAllMembers() {
        logger.info("{}", GET_MEMBER);
        List<MemberDTO> memberDTOList = memberRepository.findALlMember(fcode.backend.management.service.constant.Status.ACTIVE.toString()).stream()
                .map(member -> modelMapper.map(member, MemberDTO.class)).collect(Collectors.toList());
        logger.info("Get all members successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), memberDTOList);
    }

    public Response<MemberDTO> getMemberById(Integer id) {
        logger.info("{}{}", GET_MEMBER, id);
        if (id == null) {
            logger.warn("{}{}", GET_MEMBER, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if(memberRepository.findMemberById(id) == null) {
            logger.warn("{}{}", GET_MEMBER, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        Member member = memberRepository.findMemberById(id);
        MemberDTO memberDTO = modelMapper.map(member, MemberDTO.class);
        logger.info("Get member by id successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), memberDTO);
    }

    public Response<MemberDTO> getMemberByStudentId(String studentId) {
        logger.info("{}{}", GET_MEMBER, studentId);
        if(studentId == null) {
            logger.warn("{}{}", GET_MEMBER, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if(memberRepository.findMemberByStudentId(studentId, fcode.backend.management.service.constant.Status.ACTIVE.toString()) == null) {
            logger.warn("{}{}", GET_MEMBER, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        Member member = memberRepository.findMemberByStudentId(studentId, Status.ACTIVE.toString());
        MemberDTO memberDTO = modelMapper.map(member, MemberDTO.class);
        logger.info("Get member by student Id successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), memberDTO);
    }

    public Response<List<MemberDTO>> getMemberByLastname(String lastname) {
        logger.info("{}{}", GET_MEMBER, lastname);
        if(lastname == null) {
            logger.warn("{}{}", GET_MEMBER, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if(!memberRepository.existsByLastNameAndStatus(lastname, Status.ACTIVE) ) {
            logger.warn("{}{}", GET_MEMBER, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        List<MemberDTO> memberDTOList = memberRepository.findMemberByLastname(lastname, Status.ACTIVE.toString()).stream()
                .map(member -> modelMapper.map(member, MemberDTO.class)).collect(Collectors.toList());
        logger.info("Get member by lastname successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), memberDTOList);
    }

    public Response<List<MemberDTO>> getMemberByPosition(Integer id) {
        logger.info("{}{}", GET_MEMBER, id);
        if(id == null) {
            logger.warn("{}{}", GET_MEMBER, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        if(!positionRepository.existsById(id)) {
            logger.warn("{}{}", GET_MEMBER, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        List<MemberDTO> memberDTOList = memberRepository.getMembersByPositionIdAndStatus(id, Status.ACTIVE.toString())
                        .stream().map(member -> modelMapper.map(member, MemberDTO.class)).collect(Collectors.toList());
        logger.info("Get member by position Id successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage(), memberDTOList);
    }

    public Response<Void> updateForAdmin(MemberDTO memberDTO){
        logger.info("{}{}", UPDATE_MEMBER, memberDTO);
        if(memberDTO == null) {
            logger.warn("{}{}", UPDATE_MEMBER, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Member member = memberRepository.findMemberByIdAndStatus(memberDTO.getId(), Status.ACTIVE);
        if(member == null) {
            logger.warn("{}{}", UPDATE_MEMBER, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        if(memberDTO.getRole() != null) {
            member.setRole(memberDTO.getRole());
        }
        if(memberDTO.getPositionId() != null ) {
            if(positionRepository.existsById(memberDTO.getPositionId())) {
                member.setPosition(new Position(memberDTO.getPositionId()));
            } else return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }

        if(memberDTO.getCrewId() != null) {
            if(crewRepository.existsById(memberDTO.getCrewId())) {
                member.setCrew(new Crew(memberDTO.getCrewId()));
            } else return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        memberRepository.save(member);
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> updateForMember(MemberDTO memberDTO, Integer memberId) {
        logger.info("{}{}", UPDATE_MEMBER, memberDTO);
        if(memberDTO == null || memberId == null) {
            logger.warn("{}{}", UPDATE_MEMBER, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Member member = memberRepository.findMemberByIdAndStatus(memberId, Status.ACTIVE);
        if(member == null) {
            logger.warn("{}{}", UPDATE_MEMBER, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        if(memberDTO.getStudentId() != null && studentIdPattern.matcher(memberDTO.getStudentId()).find()) {
            member.setStudentId(memberDTO.getStudentId());
        }
        if(memberDTO.getFirstName() != null) {
            member.setFirstName(memberDTO.getFirstName());
        }
        if(memberDTO.getLastName() != null) {
            member.setLastName(memberDTO.getLastName());
        }
        if(memberDTO.getAvatarUrl() != null) {
            member.setAvatarUrl(memberDTO.getAvatarUrl());
        }
        if(memberDTO.getMajor() != null) {
            member.setMajor(memberDTO.getMajor());
        }
        if(memberDTO.getDateOfBirth() != null) {
            member.setDateOfBirth(memberDTO.getDateOfBirth());
        }
        if(memberDTO.getClubEntryDate() != null) {
            member.setClubEntryDate(memberDTO.getClubEntryDate());
        }
        if(memberDTO.getPhone() != null) {
            member.setPhone(memberDTO.getPhone());
        }
        if(memberDTO.getPersonalMail() != null
                && mailPattern.matcher(memberDTO.getPersonalMail()).find()
                && memberDTO.getSchoolMail() == null) {
            String randomCode = RandomString.make(32);
            if(member.getVerificationCode() == null || member.getVerificationCode().isEmpty()) {
                member.setVerificationCode("&");
            }
            String verifyCode = member.getVerificationCode();
            String subCode = member.getVerificationCode().substring(0, member.getVerificationCode().indexOf("&"));
            if(!subCode.isEmpty()) {
                verifyCode = verifyCode.replace(subCode, randomCode);
                member.setVerificationCode(verifyCode);
            } else {
                verifyCode = randomCode + verifyCode;
                member.setVerificationCode(verifyCode);
            }
        }
        if(memberDTO.getSchoolMail() != null
                && mailPattern.matcher(memberDTO.getSchoolMail()).find()
                && memberDTO.getSchoolMail().endsWith(studentEmailDomain)
                && memberDTO.getPersonalMail() == null) {
            String randomCode = RandomString.make(32);
            if(member.getVerificationCode() == null || member.getVerificationCode().isEmpty()) {
                member.setVerificationCode("&");
            }
            String verifyCode = member.getVerificationCode();
            String subCode = member.getVerificationCode().substring(member.getVerificationCode().indexOf("&")+1);
            if(!subCode.isEmpty()) {
                verifyCode = verifyCode.replace(subCode, randomCode);
                member.setVerificationCode(verifyCode);
            } else {
                verifyCode = verifyCode + randomCode;
                member.setVerificationCode(verifyCode);
            }
        }
        if(memberDTO.getFacebookUrl() != null) {
            member.setFacebookUrl(memberDTO.getFacebookUrl());
        }
        if(memberDTO.getDescription() != null) {
            member.setDescription(memberDTO.getDescription());
        }

        memberRepository.save(member);
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> deleteMember(Integer id) {
        logger.info("{}{}", DELETE_MEMBER, id);
        if(id == null) {
            logger.warn("{}{}", DELETE_MEMBER, ServiceMessage.INVALID_ARGUMENT_MESSAGE);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
        Member memberEntity = memberRepository.findMemberByIdAndStatus(id, Status.ACTIVE);
        if(memberEntity == null) {
            logger.warn("{}{}", DELETE_MEMBER, ServiceMessage.ID_NOT_EXIST_MESSAGE);
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
        }
        memberEntity.setStatus(Status.INACTIVE);
        memberRepository.save(memberEntity);
        logger.info("Delete member successfully");
        return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
    }

    public Response<Void> sendVerificationPersonalEmail(Member member, MemberDTO memberDTO, String siteURL, String action) throws MessagingException, UnsupportedEncodingException {
            String subjectRe = "Please verify your registration";
            String subjectUp = "Please verify your update";
            String senderName = "F-Code";
            String content = "<p>Dear " + member.getLastName() + ",</p>";
            content += "<p>Please click the link below to verify:</p>";
            String verifyURL = siteURL + "/member/verifyPMail/" + member.getVerificationCode();
            content += "<h3><a href=\"" + verifyURL + "\">VERIFY</a></h3>";
            content += "<p>Thank you,<br>F-Code Club.</p>";

            try {
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message,"utf-8");

                helper.setFrom(sender, senderName);
                helper.setTo(memberDTO.getPersonalMail());
                if(action.equals("registration")) {
                    helper.setSubject(subjectRe);
                }
                if(action.equals("update")) {
                    helper.setSubject(subjectUp);
                }
                helper.setText(content, true);

                javaMailSender.send(message);

                logger.info("Send mail successfully");
                return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
            } catch (Exception e) {
                return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
            }
    }
    public Response<Void> sendVerificationSchoolEmail(Member member, MemberDTO memberDTO, String siteURL, String action) throws MessagingException, UnsupportedEncodingException {
        String subjectRe = "Please verify your registration";
        String subjectUp = "Please verify your update";
        String senderName = "F-Code";
        String content = "<p>Dear " + member.getLastName() + ",</p>";
        content += "<p>Please click the link below to verify:</p>";
        String verifyURL = siteURL + "/member/verifySMail/" + member.getVerificationCode();
        content += "<h3><a href=\"" + verifyURL + "\">VERIFY</a></h3>";
        content += "<p>Thank you,<br>F-Code Club.</p>";

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,"utf-8");

            helper.setFrom(sender, senderName);
            helper.setTo(memberDTO.getSchoolMail());
            if(action.equals("registration")) {
                helper.setSubject(subjectRe);
            }
            if(action.equals("update")) {
                helper.setSubject(subjectUp);
            }
            helper.setText(content, true);

            javaMailSender.send(message);

            logger.info("Send mail successfully");
            return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
        } catch (Exception e) {
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
    }
    public Response<Void> verifyPersonalMail(String verificationCode, String newMail) {
        Member member = memberRepository.findByVerificationCode(verificationCode);
        if(member == null) {
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        } else {
            if(!member.getPersonalMail().equals(newMail)) {
                member.setPersonalMail(newMail);
                memberRepository.save(member);
                return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
            } else return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
    }
    public Response<Void> verifySchoolMail(String verificationCode, String newMail) {
        Member member = memberRepository.findByVerificationCode(verificationCode);
        if(member == null) {
            return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        } else {
            if(!member.getSchoolMail().equals(newMail)) {
                member.setSchoolMail(newMail);
                memberRepository.save(member);
                return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
            } else return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
    }
}
