package com.dtbafrica.event_service.repository;

import com.dtbafrica.event_service.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
    List<Notification> findByTransactionId(String transactionId);
}