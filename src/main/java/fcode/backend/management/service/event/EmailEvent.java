package fcode.backend.management.service.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Set;

@Getter
public class EmailEvent extends ApplicationEvent {
    private Set<String> emailSet;
    private String mailTitle;
    private String mail;
    public EmailEvent(Object source, Set<String> emailSet, String mailTitle, String mail) {
        super(source);
        this.emailSet = emailSet;
        this.mailTitle = mailTitle;
        this.mail = mail;
    }
}
