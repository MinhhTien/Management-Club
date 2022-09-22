package fcode.backend.management.service;

import fcode.backend.management.model.dto.EmailDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") private String sender;

    public String sendHtmlEmail(EmailDetail detail) {
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
}
