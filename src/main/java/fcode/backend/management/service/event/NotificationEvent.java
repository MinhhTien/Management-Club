package fcode.backend.management.service.event;

import fcode.backend.management.model.dto.NotificationDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Set;

@Getter
public class NotificationEvent extends ApplicationEvent {
    private Set<String> emailSet;
    private NotificationDTO notificationDTO;
    public NotificationEvent(Object source, Set<String> emailSet, NotificationDTO notificationDTO) {
        super(source);
        this.emailSet = emailSet;
        this.notificationDTO = notificationDTO;
    }
}
