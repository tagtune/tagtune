package com.ll.tagtune.boundedContext.music.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMusic is a Querydsl query type for Music
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMusic extends EntityPathBase<Music> {

    private static final long serialVersionUID = -1789041534L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMusic music = new QMusic("music");

    public final com.ll.tagtune.base.baseEntity.QBaseEntity _super = new com.ll.tagtune.base.baseEntity.QBaseEntity(this);

    public final com.ll.tagtune.boundedContext.album.entity.QAlbum album;

    public final com.ll.tagtune.boundedContext.artist.entity.QArtist artist;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final StringPath title = createString("title");

    public final NumberPath<Integer> trackNumber = createNumber("trackNumber", Integer.class);

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QMusic(String variable) {
        this(Music.class, forVariable(variable), INITS);
    }

    public QMusic(Path<? extends Music> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMusic(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMusic(PathMetadata metadata, PathInits inits) {
        this(Music.class, metadata, inits);
    }

    public QMusic(Class<? extends Music> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.album = inits.isInitialized("album") ? new com.ll.tagtune.boundedContext.album.entity.QAlbum(forProperty("album")) : null;
        this.artist = inits.isInitialized("artist") ? new com.ll.tagtune.boundedContext.artist.entity.QArtist(forProperty("artist")) : null;
    }

}

