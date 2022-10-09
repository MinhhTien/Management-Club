package fcode.backend.management.controller;

import fcode.backend.management.model.dto.NotificationDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/")
public class NotificationController {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping(value = "/template")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @MessageMapping("/message")
    @SendTo("/queue/messages")
    public NotificationDTO getMessage(final NotificationDTO notificationDTO) {
        return notificationDTO;
    }

    @MessageMapping("/private-message")
    public void getPrivateMessage(SimpMessageHeaderAccessor smha, @Payload String userEmail) {
        if (smha.getUser() == null || smha.getUser().getName() == null) return;
        String message = "Hello from " + smha.getUser().getName();
        simpMessagingTemplate.convertAndSendToUser(userEmail, "/queue/private-messages", message);
    }
}
