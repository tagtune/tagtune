package com.ll.tagtune.boundedContext.playlist.service;

import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.member.repository.MemberRepository;
import com.ll.tagtune.boundedContext.playlist.entity.Playlist;
import com.ll.tagtune.boundedContext.playlist.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private MemberRepository memberRepository;

    public List<Playlist> findAllByMemberId(final Long memberId) {
        return playlistRepository.findAllByMember_id(memberId);
    }

    public Optional<Playlist> findById(final Long id) {
        return playlistRepository.findById(id);
    }

    /**
     * 해당 플레이리스트가 member의 플레이리스트가 맞는지 확인하는 함수
     *
     * @param id 플레이리스트의 Id
     * @param memberId 멤버의 Id
     * @return RsData<Playlist>
     */
    private RsData<Playlist> isValid(final Long id, final Long memberId){
        Optional<Playlist> playlistOptional = playlistRepository.findById(id);
        if (playlistOptional.isEmpty()) return RsData.of("F-1", "해당하는 플레이리스트가 없습니다.");
        if (!playlistOptional.get().getMember().getId().equals(memberId)) return RsData.of("F-2", "수정할 권한이 없습니다.");

        return RsData.successOf(playlistOptional.get());
    }
    public RsData<Playlist> createPlaylist(String name, Member member) {
        Playlist playlist = Playlist
                .builder()
                .name(name)
                .member(member)
                .build();
        playlistRepository.save(playlist);

        return RsData.of("S-1", "플레이리스트 생성이 완료되었습니다.", playlist);
    }

    public RsData<Playlist> deletePlaylist(final Long id, final Long memberId) {
        RsData<Playlist> rsPlaylist= isValid(id,memberId);
        if(rsPlaylist.isFail()) return rsPlaylist;
        playlistRepository.delete(rsPlaylist.getData());

        return RsData.of("S-1", "플레이리스트 삭제가 완료되었습니다.");
    }

    /**
     * @param id       = 멤버의 플레이리스트 id
     * @param memberId = 멤버의 id
     * @return ex : 철수의 id와 철수가 생성한 플레이리스트의 id를 매개변수로 받는다.
     */
    public RsData<Playlist> modifyPlaylist(final Long id, final Long memberId) {
        RsData<Playlist> rsPlaylist= isValid(id,memberId);
        if(rsPlaylist.isFail()) return rsPlaylist;
        // Todo : 수정 할 필드 작성

        return RsData.of("S-1", "플레이리스트 수정이 완료되었습니다.", rsPlaylist.getData());
    }
}
