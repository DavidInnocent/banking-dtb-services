package com.dtbafrica.event_service.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Accessors(chain = true)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private String transactionId;
    
    // EMAIL, SMS are the required field types TODO refactor to enum
    @Column(nullable = false)
    private String type;
    
    // PENDING, SENT, FAILED required ones TODO refacto to enum
    @Column(nullable = false)
    private String status;
    
    @Column(nullable = false)
    private String content;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}