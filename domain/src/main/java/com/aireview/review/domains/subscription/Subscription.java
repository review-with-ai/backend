package com.aireview.review.domains.subscription;

import com.aireview.review.domains.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "userId")
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

    @Column(name = "status", columnDefinition = "enum('ACTIVE','CANCEL','EXPIRED')", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "sid", columnDefinition = "varchar(100)", nullable = false, updatable = false)
    private String sid;

    @Column(name = "partner_user_id", columnDefinition = "varchar(50)", nullable = false, updatable = false)
    private String partnerUserId;

    public enum Status {
        ACTIVE, EXPIRED, CANCEL;
    }

    public static Subscription of(Long userId, String sid, String partnerUserId) {
        LocalDate now = LocalDate.now();
        LocalDateTime startDate = LocalDateTime.of(now, LocalTime.MIN);
        LocalDateTime endDate = LocalDateTime.of(now.plusYears(100), LocalTime.MAX);

        return new Subscription(
                userId,
                startDate,
                endDate,
                null,
                Status.ACTIVE,
                sid,
                partnerUserId
        );
    }

    public Subscription(Long userId, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime cancelledAt, Status status, String sid, String partnerUserId) {
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.cancelledAt = cancelledAt;
        this.status = status;
        this.sid = sid;
        this.partnerUserId = partnerUserId;
    }

}
