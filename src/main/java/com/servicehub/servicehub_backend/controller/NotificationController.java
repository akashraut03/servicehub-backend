package com.servicehub.servicehub_backend.controller;

import com.servicehub.servicehub_backend.dto.NotificationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j // Add Lombok for logging
public class NotificationController {

    @MessageMapping("/notify")
    @SendTo("/topic/notifications")
    public NotificationDTO sendNotification(String message) {
        log.info("📩 Received WebSocket message: {}", message);
        return new NotificationDTO("hello");
    }
}
