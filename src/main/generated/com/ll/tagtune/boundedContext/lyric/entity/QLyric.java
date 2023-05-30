package com.ll.tagtune.boundedContext.lyric.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLyric is a Querydsl query type for Lyric
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLyric extends EntityPathBase<Lyric> {

    private static final long serialVersionUID = 836031170L;

    public static final QLyric lyric = new QLyric("lyric");

    public final com.ll.tagtune.base.baseEntity.QBaseEntity _super = new com.ll.tagtune.base.baseEntity.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public QLyric(String variable) {
        super(Lyric.class, forVariable(variable));
    }

    public QLyric(Path<? extends Lyric> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLyric(PathMetadata metadata) {
        super(Lyric.class, metadata);
    }

}

