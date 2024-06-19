package com.aireview.review.domains;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRevInfo is a Querydsl query type for RevInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRevInfo extends EntityPathBase<RevInfo> {

    private static final long serialVersionUID = -977189315L;

    public static final QRevInfo revInfo = new QRevInfo("revInfo");

    public final NumberPath<Long> rev = createNumber("rev", Long.class);

    public final NumberPath<Long> timestamp = createNumber("timestamp", Long.class);

    public QRevInfo(String variable) {
        super(RevInfo.class, forVariable(variable));
    }

    public QRevInfo(Path<? extends RevInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRevInfo(PathMetadata metadata) {
        super(RevInfo.class, metadata);
    }

}

