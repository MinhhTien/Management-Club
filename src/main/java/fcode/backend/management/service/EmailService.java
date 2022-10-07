package fcode.backend.management.service;

import fcode.backend.management.model.dto.EmailDetailDTO;
import fcode.backend.management.repository.AttendanceRepository;
import fcode.backend.management.repository.MemberRepository;
import fcode.backend.management.service.constant.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
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
    ModelMapper modelMapper;

    @Value("${spring.mail.username}") private String sender;

    private static final Logger logger = LogManager.getLogger(EmailService.class);
    private static final String SEND_HTML_EMAIL = "Send Html Email: ";

    @Transactional
    public void sendHtmlEmail(EmailDetailDTO detail) {
        logger.info("{}{}", SEND_HTML_EMAIL, detail);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try{
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            mimeMessage.setContent(detail.getMsgBody(), "text/html");
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(detail.getRecipient());
            mimeMessageHelper.setSubject(detail.getSubject());

            javaMailSender.send(mimeMessage);
            logger.info("{}{}", SEND_HTML_EMAIL, "Mail sent Successfully");
        } catch (MessagingException e) {
            logger.error("{}{}", SEND_HTML_EMAIL, "Error while sending mail!!!");
        }
    }

    public String inputInfoToHtml(String html, String studentId, String name) {
        return html.replace("${studentId}", studentId).replace("${name}", name);
    }
}
