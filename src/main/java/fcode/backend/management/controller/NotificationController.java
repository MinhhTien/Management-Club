package fcode.backend.management.controller;

import fcode.backend.management.model.dto.Message;
import fcode.backend.management.model.dto.ResponseMessage;
import fcode.backend.management.service.NotificationService;
import fcode.backend.management.service.WebsocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;

@RestController
@RequestMapping("/")
public class NotificationController {
    @Autowired
    NotificationService notificationService;

    @Autowired
    WebsocketService websocketService;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping(value = "/template")
    public ModelAndView  index() {
        //String msg = "Welcome to Thymeleaf Template";
        // adding the attribute(key-value pair)
       // model.addAttribute("message", msg);
        // returning the view name
        return new ModelAndView("index");
    }

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public ResponseMessage getMessage(final Message message) throws InterruptedException {
        Thread.sleep(1000);

        notificationService.sendGlobalNotification();
        return new ResponseMessage(HtmlUtils.htmlEscape(message.getMessageContent()));
    }

    @MessageMapping("/private-message")
    @SendToUser("/topic/private-messages")
    public ResponseMessage getPrivateMessage(final Message message,
                                             final Principal principal) throws InterruptedException {
        Thread.sleep(1000);

        notificationService.sendPrivateNotification(principal.getName());
        return new ResponseMessage(HtmlUtils.htmlEscape(
                "Sending private message to user" + principal.getName() + ": " + message.getMessageContent()));
    }

    @PostMapping("/send-message")
    public void sendMessage(@RequestBody final Message message) throws Exception {
        websocketService.notifyFrontend(message.getMessageContent());
    }

    @PostMapping("/send-private-message/{id}")
    public void sendPrivateMessage(@PathVariable final String id,
                                   @RequestBody final Message message) throws Exception {
        websocketService.notifyUser(id, message.getMessageContent());
    }
}
