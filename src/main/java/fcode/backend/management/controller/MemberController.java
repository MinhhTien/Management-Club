package fcode.backend.management.controller;

import fcode.backend.management.config.Utility;
import fcode.backend.management.config.interceptor.Regex;
import fcode.backend.management.model.dto.MemberDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.repository.MemberRepository;
import fcode.backend.management.repository.entity.Member;
import fcode.backend.management.service.MemberService;
import fcode.backend.management.service.constant.ServiceMessage;
import fcode.backend.management.service.constant.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "/member")
public class MemberController {
    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Value("${student.email.domain}")
    private String studentEmailDomain;

    Pattern mailPattern = Pattern.compile(Regex.MAIL_PATTERN);

    private String newPersonalMail;
    private String newSchoolMail;

    @GetMapping(value = "/all")
    public Response<List<MemberDTO>> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping(value = "memberId/{memberId}")
    public Response<MemberDTO> getMemberById(@PathVariable Integer memberId) {
        return memberService.getMemberById(memberId);
    }

    @GetMapping(value = "/studentId/{studentId}")
    public Response<MemberDTO> getMemberByStudentId(@PathVariable String studentId) {
        return memberService.getMemberByStudentId(studentId);
    }

    @GetMapping(value = "lastName/{lastName}")
    public Response<List<MemberDTO>> getMemberByLastName(@PathVariable String lastName) {
        return memberService.getMemberByLastname(lastName);
    }

    @GetMapping(value = "positionId/{positionId}")
    public Response<List<MemberDTO>> getMemberByPositionId(@PathVariable Integer positionId) {
        return memberService.getMemberByPosition(positionId);
    }

    @PutMapping(value = "/us")
    public Response<Void> updateForMember(@RequestBody MemberDTO memberDTO, @RequestAttribute(required = false) Integer userId, HttpServletRequest request) {
        try {
            Member member = memberRepository.findMemberByIdAndStatus(userId, Status.ACTIVE);
            if(member == null) {
                return new Response<>(HttpStatus.NOT_FOUND.value(), ServiceMessage.ID_NOT_EXIST_MESSAGE.getMessage());
            }
            memberService.updateForMember(memberDTO, userId);
            String siteURL = Utility.getSiteURL(request);

            if(memberDTO.getPersonalMail() != null
                    && mailPattern.matcher(memberDTO.getPersonalMail()).find()
                    && memberDTO.getSchoolMail() == null) {
                memberService.sendVerificationPersonalEmail(member, memberDTO, siteURL, "update");
                newPersonalMail = memberDTO.getPersonalMail();
            }
            if(memberDTO.getSchoolMail() != null
                    && mailPattern.matcher(memberDTO.getSchoolMail()).find()
                    && memberDTO.getSchoolMail().endsWith(studentEmailDomain)
                    && memberDTO.getPersonalMail() == null) {
                memberService.sendVerificationSchoolEmail(member, memberDTO, siteURL, "update");
                newSchoolMail = memberDTO.getSchoolMail();
            }
            return new Response<>(HttpStatus.OK.value(), ServiceMessage.SUCCESS_MESSAGE.getMessage());
        } catch (Exception e) {
            return new Response<>(HttpStatus.BAD_REQUEST.value(), ServiceMessage.INVALID_ARGUMENT_MESSAGE.getMessage());
        }
    }

    @GetMapping(value = "/verifyPMail/{code}")
    public Response<Void> verifyPersonalMail(@PathVariable("code") String code) {
        return memberService.verifyPersonalMail(code, newPersonalMail);
    }

    @GetMapping(value = "/verifySMail/{code}")
    public Response<Void> verifySchoolMail(@PathVariable("code") String code) {
        return memberService.verifySchoolMail(code, newSchoolMail);
    }

    @PutMapping(value = "/ad")
    public Response<Void> updateForAdmin(@RequestBody MemberDTO memberDTO) {
        return memberService.updateForAdmin(memberDTO);
    }

    @DeleteMapping(value = "id/{id}")
    public Response<Void> deleteMember(@PathVariable Integer id) {
        return memberService.deleteMember(id);
    }
}
