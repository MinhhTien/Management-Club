package fcode.backend.management.service;

import fcode.backend.management.model.dto.EmailDetailDTO;
import fcode.backend.management.repository.AttendanceRepository;
import fcode.backend.management.repository.MemberRepository;
import fcode.backend.management.service.constant.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.apache.commons.validator.GenericTypeValidator;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    GenericTypeValidator genericTypeValidator;

    @Value("${spring.mail.username}") private String sender;
    private static final String CREWID = "crewId";
    private static final String EVENTID = "eventId";
    private static final String K = "K";

    @Transactional
    public String sendHtmlEmail(EmailDetailDTO detail) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try{
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            mimeMessage.setContent(detail.getMsgBody(), "text/html");
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(detail.getRecipient());
            mimeMessageHelper.setSubject(detail.getSubject());

            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";
        } catch (MessagingException e) {
            return "Error while sending mail!!!";
        }
    }

    public String inputInfoToHtml(String html, String studentId, String name) {
        return html.replace("${studentId}", studentId).replace("${name}", name);
    }

    //valid infoUserId 123&124&345&987
    public List<Integer> parseValidInfoText(String infoUserId, String separator) {
        List<Integer> listUserId = new ArrayList<>();
        for(String id: infoUserId.split(separator)) {
            Integer userId = genericTypeValidator.formatInt(id);
            if(userId == null) return null;
            listUserId.add(userId);
        }
        return listUserId;
    }

    //valid infoGroup eventId=123&crewId=234/453&K=15/16
    public Map<String, List<Integer>> parseValidInfoGroup(String infoGroup) {
        Map<String, List<Integer>> conditionMap = new HashMap<>();
        for(String condition: infoGroup.split("&")) {
            String[] conditionArr = condition.split("=");
            if(conditionArr.length != 2) return null;
            if(conditionArr[0].equals(EVENTID) || conditionArr[0].equals(CREWID) || conditionArr[0].equals(K)) {
                List<Integer> numList = parseValidInfoText(conditionArr[1], "/");
                if(numList == null) return null;
                else conditionMap.put(conditionArr[0],numList);
            } else return null;
        }
        return conditionMap;
    }

    public Set<String> parseInfoUserIdToEmail(List<Integer> userIdList) {
        Set<String> emailList = new HashSet<>();
        for(Integer userId : userIdList) {
            String email = memberRepository.getEmailById(userId, Status.ACTIVE.toString());
            if(email == null) return null;
            emailList.add(email);
        }
        return emailList;
    }

    public Set<String> parseInfoGroupToEmail(Map<String, List<Integer>> groupConditionMap) {
        Set<String> emailList = new HashSet<>();
        if(groupConditionMap.containsKey(EVENTID)) {
            for(Integer eventId: groupConditionMap.get(EVENTID)) {
                List<String> email = attendanceRepository.getEmailsByEventId(eventId);
                if(email == null) return null;
                emailList.addAll(email);
            }
        }
        if(groupConditionMap.containsKey(CREWID)) {
            for(Integer crewId: groupConditionMap.get(CREWID)) {
                List<String> email = memberRepository.getEmailsByCrewId(crewId, Status.ACTIVE.toString());
                if(email == null) return null;
                emailList.addAll(email);
            }
        }
        if(groupConditionMap.containsKey(K)) {
            for(Integer Kxx: groupConditionMap.get(K)) {
                List<String> email = memberRepository.getEmailsByK("__ %".replace(" ", Kxx.toString()), Status.ACTIVE.toString());
                if(email == null) return null;
                emailList.addAll(email);
            }
        }
        return emailList;
    }
}
