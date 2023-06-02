package com.ll.tagtune.boundedContext.album.service;

import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.album.dto.AlbumDTO;
import com.ll.tagtune.boundedContext.album.entity.Album;
import com.ll.tagtune.boundedContext.album.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;

    public List<Album> findAllByName(final String name) {
        return albumRepository.findAllByName(name);
    }

    public Optional<Album> findById(final Long id) {
        return albumRepository.findById(id);
    }

    /**
     * 유저가 앨범을 접근할 때 사용하는 메소드입니다.
     *
     * @param id
     * @return RsData<Album>
     */
    public RsData<Album> getAlbum(final Long id) {
        return findById(id)
                .map(RsData::successOf)
                .orElseGet(() -> RsData.of("F-1", "해당하는 앨범이 없습니다."));
    }

    public Album createAlbum(final String name, final String image) {
        Album album = Album
                .builder()
                .name(name)
                .image(image)
                .build();

        return albumRepository.save(album);
    }

    public RsData<Album> deleteAlbum(Long id) {
        Optional<Album> albumOptional = albumRepository.findByid(id);

        if (albumOptional.isEmpty()) return RsData.of("F-1", "해당하는 앨범이 없습니다.");
        albumRepository.delete(albumOptional.get());

        return RsData.of("S-1", "앨범삭제가 완료되었습니다.");
    }

    /**
     * AlbumDTO 에 적합한 Album 를 생성하거나 리턴합니다.
     * <p>
     * API 에서 받아온 Album 를 반드시 받기 위해 사용합니다.
     *
     * @param albumDTO
     * @return Album
     */
    public Album getOrCreateAlbumDTO(AlbumDTO albumDTO) {
        Optional<Album> oAlbum = findAllByName(albumDTO.getName()).stream().findAny();

        return oAlbum.orElseGet(() -> createAlbum(albumDTO.getName(), albumDTO.getImage()));
    }
}
