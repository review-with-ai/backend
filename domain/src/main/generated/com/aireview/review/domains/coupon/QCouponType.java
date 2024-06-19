package com.aireview.review.domains.coupon;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCouponType is a Querydsl query type for CouponType
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCouponType extends EntityPathBase<CouponType> {

    private static final long serialVersionUID = 2129663700L;

    public static final QCouponType couponType = new QCouponType("couponType");

    public final com.aireview.review.domains.QBaseAuditEntity _super = new com.aireview.review.domains.QBaseAuditEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final EnumPath<CouponType.Status> status = createEnum("status", CouponType.Status.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final NumberPath<Long> updatedBy = _super.updatedBy;

    public QCouponType(String variable) {
        super(CouponType.class, forVariable(variable));
    }

    public QCouponType(Path<? extends CouponType> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCouponType(PathMetadata metadata) {
        super(CouponType.class, metadata);
    }

}

