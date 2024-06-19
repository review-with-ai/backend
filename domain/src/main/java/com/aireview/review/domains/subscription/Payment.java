package com.aireview.review.domains.subscription;

import com.aireview.review.domains.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = {"subscriptionId", "seq"})
@ToString
public class Payment extends BaseAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subscription_id", nullable = false, updatable = false)
    private Long subscriptionId;

    @Column(name = "tid", columnDefinition = "varchar(100)", nullable = false, updatable = false)
    private String tid;

    @Column(name = "sid", columnDefinition = "varchar(100)", updatable = false)
    private String sid;

    @Column(name = "order_id", columnDefinition = "varchar(100)", updatable = false)
    private String orderId;

    @Column(name = "amount", columnDefinition = "int unsigned", nullable = false, updatable = false)
    private int amount;

    @Column(name = "seq", columnDefinition = "tinyint unsigned", nullable = false, updatable = false)
    private byte seq;

    @Column(name = "event_type", columnDefinition = "varchar(30)", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Column(name = "details", columnDefinition = "varchar(100)")
    private String details;

    @Column(name = "timestamp", columnDefinition = "datetime", updatable = false, nullable = false)
    private LocalDateTime timestamp;

    public enum EventType {
        SUCCESS, FAIL, CANCELLED;
    }

    public Payment(Long subscriptionId, String tid, String sid, String orderId, int amount, byte seq, EventType eventType, String details, LocalDateTime timestamp) {
        this.subscriptionId = subscriptionId;
        this.tid = tid;
        this.sid = sid;
        this.amount = amount;
        this.orderId = orderId;
        this.seq = seq;
        this.eventType = eventType;
        this.details = details;
        this.timestamp = timestamp;
    }
}
