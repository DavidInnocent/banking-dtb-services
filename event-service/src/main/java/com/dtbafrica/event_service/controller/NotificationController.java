package com.dtbafrica.event_service.controller;

import com.dtbafrica.event_service.model.Notification;
import com.dtbafrica.event_service.repository.NotificationRepository;
import com.netflix.discovery.converters.Auto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    @Autowired
    NotificationRepository notificationRepository;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<List<Notification>> getTransactionNotifications(
            @PathVariable String transactionId) {
        List<Notification> notifications = notificationRepository.findByTransactionId(transactionId);
        return ResponseEntity.ok(notifications);
    }
}