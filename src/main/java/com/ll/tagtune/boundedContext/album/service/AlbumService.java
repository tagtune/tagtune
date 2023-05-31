package com.ll.tagtune.boundedContext.album.service;

import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.album.entity.Album;
import com.ll.tagtune.boundedContext.album.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;

    public RsData<Album> createAlbum(String name, String image) {

        Album album = Album
                .builder()
                .name(name)
                .createDate(LocalDateTime.now())
                .modifyDate(LocalDateTime.now())
                .image(image)
                .build();

        albumRepository.save(album);

        return RsData.of("S-1", "앨범생성이 완료되었습니다.", album);
    }

    public RsData<Album> deleteAlbum(Long id) {
        Optional<Album> albumOptional = albumRepository.findByid(id);

        if (albumOptional.isPresent()) {
            Album album = albumOptional.get();
            albumRepository.delete(album);

            return RsData.of("S-2", "앨범이 삭제되었습니다.");
        } else {
            return RsData.of("F-1", "해당하는 앨범이 없습니다.");
        }
    }

    public Long albumCount()
    {
        return albumRepository.count();
    }

}
