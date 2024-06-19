package com.aireview.review.domains;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseAuditEntity {

    @Column(name = "created_at", columnDefinition = "datetime", updatable = false, nullable = false)
    @CreatedDate
    protected LocalDateTime createdAt;

    @Column(name = "created_by", columnDefinition = "bigint", nullable = false, updatable = false)
    @CreatedBy
    protected Long createdBy;

    @Column(name = "updated_at", columnDefinition = "datetime", nullable = false)
    @LastModifiedDate
    protected LocalDateTime updatedAt;

    @Column(name = "updated_by", columnDefinition = "bigint", nullable = false)
    @LastModifiedBy
    protected Long updatedBy;

}
