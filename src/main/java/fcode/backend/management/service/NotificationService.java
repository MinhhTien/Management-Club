package fcode.backend.management.service;

import fcode.backend.management.repository.AttendanceRepository;
import fcode.backend.management.repository.MemberRepository;
import fcode.backend.management.repository.entity.Announcement;
import fcode.backend.management.repository.entity.Member;
import fcode.backend.management.service.constant.Status;
import org.apache.commons.validator.GenericTypeValidator;
import org.apache.commons.validator.GenericValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class NotificationService {
    private static final String CREW_ID = "crewId";
    private static final String EVENT_ID = "eventId";
    private static final String K = "K";
    private static final Logger logger = LogManager.getLogger(NotificationService.class);

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    MemberRepository memberRepository;

    public Set<String> getEmailListOfNotificationReceivers(String infoUserId, String infoGroup) {
        Set<String> emailSet = new HashSet<>();
        if (!GenericValidator.isBlankOrNull(infoUserId)) {
            List<Integer> userIdList = parseValidInfoText(infoUserId, "&");
            if (!userIdList.isEmpty()) {
                emailSet = getEmailSetFromInfoUserId(userIdList);
            }
        }

        if (!GenericValidator.isBlankOrNull(infoGroup)) {
            Map<String, List<Integer>> groupConditionMap = parseValidInfoGroup(infoGroup);
            if (!groupConditionMap.isEmpty()) {
                emailSet.addAll(getEmailSetFromInfoGroup(groupConditionMap));
            }
        }
        return emailSet;
    }

    //valid infoUserId 123&124&345&987
    private List<Integer> parseValidInfoText(String infoUserId, String separator) {
        List<Integer> listUserId = new ArrayList<>();
        for (String id : infoUserId.split(separator)) {
            Integer userId = GenericTypeValidator.formatInt(id);
            if (userId == null) return new ArrayList<>();
            listUserId.add(userId);
        }
        return listUserId;
    }

    //valid infoGroup eventId=123&crewId=234/453&K=15/16
    private Map<String, List<Integer>> parseValidInfoGroup(String infoGroup) {
        Map<String, List<Integer>> conditionMap = new HashMap<>();
        for (String condition : infoGroup.split("&")) {
            String[] conditionArr = condition.split("=");
            if (conditionArr.length != 2) return new HashMap<>();
            if (conditionArr[0].equals(EVENT_ID) || conditionArr[0].equals(CREW_ID) || conditionArr[0].equals(K)) {
                List<Integer> numList = parseValidInfoText(conditionArr[1], "/");
                if (numList.isEmpty()) return new HashMap<>();
                else conditionMap.put(conditionArr[0], numList);
            } else return new HashMap<>();
        }
        return conditionMap;
    }

    private Set<String> getEmailSetFromInfoUserId(List<Integer> userIdList) {
        Set<String> emailList = new HashSet<>();
        for (Integer userId : userIdList) {
            String email = memberRepository.getEmailById(userId, Status.ACTIVE.toString());
            if (email == null) return new HashSet<>();
            emailList.add(email);
        }
        return emailList;
    }

    private Set<String> getEmailSetFromInfoGroup(Map<String, List<Integer>> groupConditionMap) {
        Set<String> emailList = new HashSet<>();
        if (groupConditionMap.containsKey(EVENT_ID)) {
            for (Integer eventId : groupConditionMap.get(EVENT_ID)) {
                List<String> email = attendanceRepository.getEmailsByEventId(eventId);
                if (email == null) return new HashSet<>();
                emailList.addAll(email);
            }
        }
        if (groupConditionMap.containsKey(CREW_ID)) {
            for (Integer crewId : groupConditionMap.get(CREW_ID)) {
                List<String> email = memberRepository.getEmailsByCrewId(crewId, Status.ACTIVE.toString());
                if (email == null) return new HashSet<>();
                emailList.addAll(email);
            }
        }
        if (groupConditionMap.containsKey(K)) {
            for (Integer Kxx : groupConditionMap.get(K)) {
                List<String> email = memberRepository.getEmailsByK("__ %".replace(" ", Kxx.toString()), Status.ACTIVE.toString());
                if (email == null) return new HashSet<>();
                emailList.addAll(email);
            }
        }
        return emailList;
    }

    @Transactional
    public void addNotificationToMember(Announcement announcement, Set<String> emailSet) {
        emailSet.forEach(email -> {
            logger.info("{}{}", "Add notification to member: ", email);
            Member member = memberRepository.findMemberByEmail(email);
            if (member != null) {
                member.addNotification(announcement);
                memberRepository.save(member);
                logger.info("{}{}", "Created notification to: ", member);
            }
        });
    }

    @Transactional
    public void deleteNotificationFromMember(Announcement announcement, Set<String> emailSet) {
        emailSet.forEach(email -> {
            logger.info("{}{}", "Delete notification of member: ", email);
            Member member = memberRepository.findMemberByEmail(email);
            if (member != null) {
                member.removeNotification(announcement);
                memberRepository.save(member);
                logger.info("{}{}", "Deleted notification out of: ", member);
            }
        });
    }
}
