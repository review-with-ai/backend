package com.aireview.review.domains.subscription;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSubscription is a Querydsl query type for Subscription
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSubscription extends EntityPathBase<Subscription> {

    private static final long serialVersionUID = 1845090778L;

    public static final QSubscription subscription = new QSubscription("subscription");

    public final com.aireview.review.domains.QBaseAuditEntity _super = new com.aireview.review.domains.QBaseAuditEntity(this);

    public final DateTimePath<java.time.LocalDateTime> cancelledAt = createDateTime("cancelledAt", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath partnerUserId = createString("partnerUserId");

    public final StringPath sid = createString("sid");

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    public final EnumPath<Subscription.Status> status = createEnum("status", Subscription.Status.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final NumberPath<Long> updatedBy = _super.updatedBy;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QSubscription(String variable) {
        super(Subscription.class, forVariable(variable));
    }

    public QSubscription(Path<? extends Subscription> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSubscription(PathMetadata metadata) {
        super(Subscription.class, metadata);
    }

}

