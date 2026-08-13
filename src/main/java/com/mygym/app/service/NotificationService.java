package com.mygym.app.service;

import static com.mygym.app.util.ResourceConstants.NOTIFICATIONS_CRUD_BEAN;
import static com.mygym.app.util.ResourceConstants.NOTIFICATIONS_ID_ENPOINT;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.mygym.app.models.Notification;

/**
 * Service managing admin broadcast notifications dispatched across client mobile applications.
 */
@Service
public class NotificationService {

    private RestClient restClient;

    public NotificationService(@Qualifier(NOTIFICATIONS_CRUD_BEAN) RestClient restClient) {
        this.restClient = restClient;
    }

    public String createNotification(Notification notification) {
        return restClient.post()
                .body(notification)
                .retrieve()
                .body(String.class);
    }

    public Notification getNotification(Long notificationId) {
        return restClient.get()
                .uri(NOTIFICATIONS_ID_ENPOINT, notificationId)
                .retrieve()
                .body(Notification.class);
    }

    public Integer updateNotification(Integer notificationId, Notification notification) {
        return restClient.put()
                .uri(NOTIFICATIONS_ID_ENPOINT, notificationId)
                .body(notification)
                .retrieve()
                .body(Integer.class);
    }
}