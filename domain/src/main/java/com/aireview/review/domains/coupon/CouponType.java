package com.aireview.review.domains.coupon;

import com.aireview.review.domains.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class CouponType extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "varchar(200)", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "varchar(500)", nullable = false)
    private String description;

    @Column(name = "status", columnDefinition = "enum('AVAILABLE','UNAVAILABLE','EXPIRED')", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        AVAILABLE, UNAVAILABLE, EXPIRED;
    }

    public static CouponType of(String name, String description) {
        return new CouponType(name, description, Status.AVAILABLE);
    }

    public CouponType(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }
}
