package edu.cmu.orderservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void send(final String message, String userId) {
        if (userId == null) {
            messagingTemplate.convertAndSend("/topic/public", message);
        } else {
            messagingTemplate.convertAndSend("/topic/" + userId, message);
        }

    }

}
