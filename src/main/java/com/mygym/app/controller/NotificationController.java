package com.mygym.app.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mygym.app.models.Notification;
import com.mygym.app.service.NotificationService;

@RestController
@RequestMapping("/api/notifications") // 🎯 Fixed: Clean absolute string path to bypass trailing-slash 403 blocks
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }
    
    // Path maps to: POST /api/notifications
    @PostMapping
    public String createNotification(@RequestBody Notification notification) {
        return service.createNotification(notification);
    }
}
