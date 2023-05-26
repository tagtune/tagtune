package com.ll.tagtune.boundedContext.music.service;

import com.ll.tagtune.boundedContext.music.repository.MusicRepository;
import com.ll.tagtune.boundedContext.music.repository.MusicRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MusicService {
    private final MusicRepository musicRepository;
    private final MusicRepositoryImpl musicRepositoryImpl;
}
