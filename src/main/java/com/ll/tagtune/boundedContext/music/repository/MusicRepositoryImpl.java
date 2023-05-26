package com.ll.tagtune.boundedContext.music.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MusicRepositoryImpl implements MusicRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
}
