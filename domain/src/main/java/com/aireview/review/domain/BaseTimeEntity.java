package com.aireview.review.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    @Column(name ="createdAt", columnDefinition = "datetime",  updatable = false, nullable = false)
    @CreatedDate
    protected LocalDateTime createdAt;

    @Column(name ="name", columnDefinition = "datetime",  nullable = false)
    @LastModifiedDate
    protected LocalDateTime updatedAt;
}
