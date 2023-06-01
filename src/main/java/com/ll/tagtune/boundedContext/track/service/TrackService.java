package com.ll.tagtune.boundedContext.track.service;

import com.ll.tagtune.boundedContext.track.repository.TrackRepository;
import com.ll.tagtune.boundedContext.track.repository.TrackRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrackService {
    private final TrackRepository trackRepository;
    private final TrackRepositoryImpl trackRepositoryImpl;
}
