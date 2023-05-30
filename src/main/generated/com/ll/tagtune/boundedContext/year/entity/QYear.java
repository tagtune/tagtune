package com.ll.tagtune.boundedContext.year.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QYear is a Querydsl query type for Year
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QYear extends EntityPathBase<Year> {

    private static final long serialVersionUID = -344163786L;

    public static final QYear year = new QYear("year");

    public final NumberPath<Integer> yearValue = createNumber("yearValue", Integer.class);

    public QYear(String variable) {
        super(Year.class, forVariable(variable));
    }

    public QYear(Path<? extends Year> path) {
        super(path.getType(), path.getMetadata());
    }

    public QYear(PathMetadata metadata) {
        super(Year.class, metadata);
    }

}

