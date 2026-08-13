package com.mygym.app.service;

import com.mygym.app.models.Notification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestBodySpec requestBodySpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    @DisplayName("Should create notification downstream and return ID string")
    void testCreateNotification_Success() {
        Notification notification = new Notification();
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.body(eq(notification))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(String.class)).thenReturn("N-901");

        String result = notificationService.createNotification(notification);

        assertEquals("N-901", result);
    }
}