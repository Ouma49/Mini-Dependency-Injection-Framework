package com.di.framework.example;

import com.di.framework.annotation.Autowired;
import com.di.framework.annotation.Component;

@Component
public class NotificationService {
    private final MessageService messageService;

    @Autowired
    public NotificationService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void sendNotification() {
        System.out.println("Sending notification: " + messageService.getMessage());
    }
} 