package com.ll.tagtune.boundedContext.memberFavor.service;

import com.ll.tagtune.boundedContext.album.entity.Album;
import com.ll.tagtune.boundedContext.artist.entity.Artist;
import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.memberFavor.entity.FavorAlbum;
import com.ll.tagtune.boundedContext.memberFavor.entity.FavorArtist;
import com.ll.tagtune.boundedContext.memberFavor.entity.FavorBase;
import com.ll.tagtune.boundedContext.memberFavor.entity.FavorTag;
import com.ll.tagtune.boundedContext.memberFavor.repository.FavorAlbumRepository;
import com.ll.tagtune.boundedContext.memberFavor.repository.FavorArtistRepository;
import com.ll.tagtune.boundedContext.memberFavor.repository.FavorTagRepository;
import com.ll.tagtune.boundedContext.tag.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FavorService {
    private final FavorArtistRepository favorArtistRepository;
    private final FavorAlbumRepository favorAlbumRepository;
    private final FavorTagRepository favorTagRepository;

    @Transactional(readOnly = true)
    public List<FavorBase> getFavorTags(final Long memberId) {
        List<FavorBase> result = new ArrayList<>();
        result.addAll(favorArtistRepository.findAllByMember_Id(memberId));
        result.addAll(favorAlbumRepository.findAllByMember_Id(memberId));
        result.addAll(favorTagRepository.findAllByMember_Id(memberId));

        return result;
    }

    public FavorArtist create(final Member member, final Artist artist) {
        FavorArtist favorArtist = FavorArtist.builder()
                .member(member)
                .data(artist)
                .build();

        favorArtistRepository.save(favorArtist);

        return favorArtist;
    }

    public FavorAlbum create(final Member member, final Album album) {
        FavorAlbum favorAlbum = FavorAlbum.builder()
                .member(member)
                .data(album)
                .build();

        favorAlbumRepository.save(favorAlbum);

        return favorAlbum;
    }

    public FavorTag create(final Member member, final Tag tag) {
        FavorTag favorTag = FavorTag.builder()
                .member(member)
                .data(tag)
                .build();

        favorTagRepository.save(favorTag);

        return favorTag;
    }
}
