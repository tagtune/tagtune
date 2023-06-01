package com.ll.tagtune.boundedContext.track.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TrackRepositoryImpl implements TrackRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
}
