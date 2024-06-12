package com.aireview.review.domains.coupon;

import com.aireview.review.domains.BaseTimeEntity;
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
public class Coupon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", columnDefinition = "varchar(50)", nullable = false, updatable = false)
    private String code;

    @ManyToOne(optional = false)
    private CouponType couponType;

    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "issued_at", columnDefinition = "datetime", nullable = false, updatable = false)
    private LocalDateTime issuedAt;

    @Column(name = "used_at", columnDefinition = "datetime")
    private LocalDateTime usedAt;

    @Column(name = "status", columnDefinition = "enum('AVAILABLE','UNAVAILABLE','USED')", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        AVAILABLE, UNAVAILABLE, USED;
    }

    public static Coupon of(CouponType couponType, String code, Long userId, LocalDateTime issuedAt) {
        return new Coupon(code, couponType, userId, issuedAt, null, Status.AVAILABLE);
    }

    public Coupon(String code, CouponType couponType, Long userId, LocalDateTime issuedAt, LocalDateTime usedAt, Status status) {
        this.code = code;
        this.couponType = couponType;
        this.userId = userId;
        this.issuedAt = issuedAt;
        this.usedAt = usedAt;
        this.status = status;
    }
}
