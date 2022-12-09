package fcode.backend.management.service.event;

import fcode.backend.management.model.dto.EmailDetailDTO;
import fcode.backend.management.model.dto.EmailReceiverDTO;
import fcode.backend.management.model.dto.NotificationDTO;
import fcode.backend.management.repository.MemberRepository;
import fcode.backend.management.service.EmailService;
import fcode.backend.management.service.constant.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EventListener {
    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EmailService emailService;

    private static final Logger logger = LogManager.getLogger(EventListener.class);
    private static final String WEBSOCKET_CLIENT_DESTINATION = "/queue/private-messages";

    @Async
    @org.springframework.context.event.EventListener
    public void sendNotificationEventListener(NotificationEvent notificationEvent) throws InterruptedException {
        Thread.sleep(1000);
        logger.info("{}{}", Thread.currentThread().getName(), ": Listened Send Notification Event!!!");
        NotificationDTO notificationDTO = notificationEvent.getNotificationDTO();
        notificationEvent.getEmailSet().forEach(email -> {
            logger.info("{}{}", "Send notification to member: ", email);
            messagingTemplate.convertAndSendToUser(email, WEBSOCKET_CLIENT_DESTINATION, notificationDTO);
        });
    }

    @Async
    @org.springframework.context.event.EventListener
    public void sendEmailEventListener(EmailEvent emailEvent) throws InterruptedException {
        Thread.sleep(1000);
        logger.info("{}{}", Thread.currentThread().getName(), ": Listened Send Email Event!!!");
        String mailTitle = emailEvent.getMailTitle();
        String mail = emailEvent.getMail();
        emailEvent.getEmailSet().forEach(email -> {
            logger.info("{}{}", "Send email to member: ", email);
            EmailReceiverDTO emailReceiverDTO = memberRepository.getReceiverByEmail(email, Status.ACTIVE);
            emailService.sendHtmlEmail(new EmailDetailDTO(email, mailTitle,
                    emailService.inputInfoToHtml(mail, emailReceiverDTO.getStudentId(),
                            emailReceiverDTO.getFirstName() + emailReceiverDTO.getLastName())));
        });
    }
}
