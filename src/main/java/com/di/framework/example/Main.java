package com.di.framework.example;

import com.di.framework.context.ApplicationContext;

public class Main {
    public static void main(String[] args) {
        // Example using XML configuration
        System.out.println("Using XML configuration:");
        ApplicationContext xmlContext = new ApplicationContext("src/main/resources/application.xml");
        NotificationService xmlNotificationService = xmlContext.getBean("notificationService", NotificationService.class);
        xmlNotificationService.sendNotification();

        // Example using annotation configuration
        System.out.println("\nUsing annotation configuration:");
        ApplicationContext annotationContext = new ApplicationContext("com.di.framework.example", true);
        NotificationService annotationNotificationService = annotationContext.getBean(NotificationService.class);
        annotationNotificationService.sendNotification();
    }
} 