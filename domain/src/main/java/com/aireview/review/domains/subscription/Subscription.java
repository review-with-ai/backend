package com.aireview.review.domain.subscription;

import com.aireview.review.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class Subscription extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "start_date", columnDefinition = "date", nullable = false, updatable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", columnDefinition = "date", nullable = false, updatable = false)
    private LocalDateTime endDate;

    @Column(name = "cancelled_at", columnDefinition = "datetime")
    private LocalDateTime cancelledAt;

    @Column(name = "status", columnDefinition = "enum('ACTIVE','CANCELLED','EXPIRED')", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        ACTIVE, CANCELLED, EXPIRED;
    }

    public static Subscription of(Long userId, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime cancelledAt) {
        return new Subscription(userId, startDate, endDate, cancelledAt, Status.ACTIVE);
    }

    public Subscription(
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            LocalDateTime cancelledAt,
            Status status) {
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.cancelledAt = cancelledAt;
        this.status = status;
    }
}
