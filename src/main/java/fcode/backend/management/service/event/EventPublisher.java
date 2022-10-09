package fcode.backend.management.service.event;

import fcode.backend.management.model.dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class EventPublisher {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    public void sendNotificationTo(Set<String> emailSet, NotificationDTO notificationDTO) {
        applicationEventPublisher.publishEvent(new NotificationEvent(this, emailSet, notificationDTO));
    }

    public void sendEmailTo(Set<String> emailSet, String mailTitle, String mail) {
        applicationEventPublisher.publishEvent(new EmailEvent(this, emailSet, mailTitle, mail));
    }
}
