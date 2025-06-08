package com.di.framework.example;

import com.di.framework.annotation.Component;

@Component
public class EmailService implements MessageService {
    @Override
    public String getMessage() {
        return "This is an email message";
    }
} 