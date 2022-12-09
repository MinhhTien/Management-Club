package fcode.backend.management.config.interceptor;

import fcode.backend.management.config.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class UserInterceptor implements ChannelInterceptor {
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if(StompCommand.CONNECT.equals(accessor.getCommand())) {
            Object raw = message.getHeaders().get(StompHeaderAccessor.NATIVE_HEADERS);

            if(raw instanceof Map) {
                Object tokens = ((Map) raw).get("token");
                if(tokens instanceof ArrayList) {
                    String token = ((ArrayList<String>) tokens).get(0);
                    String userEmail;
                    userEmail = jwtTokenUtil.validateToken(token);
                    accessor.setUser(new User(userEmail));
                }
            }
        }
        return message;
    }
}
