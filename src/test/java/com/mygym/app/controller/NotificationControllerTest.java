package com.mygym.app.controller;

import com.mygym.app.models.Notification;
import com.mygym.app.service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    private NotificationService service;

    @InjectMocks
    private NotificationController controller;

    @Test
    @DisplayName("Should create notification and return ID string")
    void testCreateNotification_Success() {
        Notification notification = new Notification();
        when(service.createNotification(any(Notification.class))).thenReturn("N-901");

        String result = controller.createNotification(notification);

        assertEquals("N-901", result);
        verify(service, times(1)).createNotification(notification);
    }
}