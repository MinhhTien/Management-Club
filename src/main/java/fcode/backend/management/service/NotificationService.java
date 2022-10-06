package fcode.backend.management.service;

import fcode.backend.management.repository.AnnouncementRepository;
import fcode.backend.management.repository.MemberRepository;
import fcode.backend.management.repository.entity.Announcement;
import fcode.backend.management.repository.entity.Member;
import org.apache.commons.validator.GenericValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class NotificationService {
    @Autowired
    AnnouncementRepository announcementRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EmailService emailService;

    private static final Logger logger = LogManager.getLogger(NotificationService.class);
    private static final String ADD_NOTIFICATION = "Create notification: ";
    private static final String DELETE_NOTIFICATION = "Delete notification: ";

    public Set<String> getEmailListOfNotificationReceivers(String infoUserId, String infoGroup) {
        Set<String> emailSet = new HashSet<>();
        if (!GenericValidator.isBlankOrNull(infoUserId)) {
            List<Integer> userIdList = emailService.parseValidInfoText(infoUserId, "&");
            if (userIdList != null) {
                emailSet = emailService.getEmailSetFromInfoUserId(userIdList);
            }
        }

        if (!GenericValidator.isBlankOrNull(infoGroup)) {
            Map<String, List<Integer>> groupConditionMap = emailService.parseValidInfoGroup(infoGroup);
            if (groupConditionMap != null) {
                emailSet.addAll(emailService.getEmailSetFromInfoGroup(groupConditionMap));
            }
        }
        return  emailSet;
    }

    @Transactional
    public void addNotificationToMember(Announcement announcement, String email) {
        Member member = memberRepository.findMemberByEmail(email);
        member.addNotification(announcement);
        memberRepository.save(member);
    }

    @Transactional
    public void deleteNotificationFromMember(Announcement announcement, Set<String> emailSet) {
        emailSet.forEach(email -> {
            Member member = memberRepository.findMemberByEmail(email);
            member.removeNotification(announcement);
            memberRepository.save(member);
        });
    }
}
