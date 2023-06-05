package com.ll.tagtune.base.lastfm;

import com.ll.tagtune.boundedContext.album.dto.AlbumDTO;
import com.ll.tagtune.boundedContext.artist.dto.ArtistDTO;
import com.ll.tagtune.boundedContext.tag.dto.TagDTO;
import com.ll.tagtune.boundedContext.track.dto.TrackInfoDTO;
import com.ll.tagtune.boundedContext.track.dto.TrackSearchDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResultParser {
    /**
     * 검색 결과를 TrackSearchDTO 으로 파싱하여 리턴합니다
     *
     * @param query 검색어
     * @return List<TrackSearchDTO> 트랙 목록
     */
    public static List<TrackSearchDTO> searchTracks(String query) {
        Map obj = SearchEndpoint.searchTrack(query);
        Map results = (Map) obj.get("results");
        LinkedHashMap trackMatches = (LinkedHashMap) results.get("trackmatches");
        List<LinkedHashMap<String, String>> rawTracks = (List<LinkedHashMap<String, String>>) trackMatches.get("track");
        List<TrackSearchDTO> tracks = new ArrayList<>();
        for (LinkedHashMap<String, String> rawTrack : rawTracks) {
            String title = rawTrack.get("name");
            ArtistDTO artist = ArtistDTO.builder()
                    .artistName(rawTrack.get("artist"))
                    .build();

            tracks.add(TrackSearchDTO.builder()
                    .title(title)
                    .artistDTO(artist)
                    .build());
        }

        return tracks;
    }

    /**
     * 검색 결과를 TrackSearchDTO 으로 파싱하여 리턴합니다
     *
     * @param title
     * @param artist
     * @return List<TrackSearchDTO> 트랙 목록
     */
    public static List<TrackSearchDTO> searchTracks(String title, String artist) {
        Map obj = SearchEndpoint.searchTrack(title, artist);
        Map results = (Map) obj.get("results");
        LinkedHashMap trackMatches = (LinkedHashMap) results.get("trackmatches");
        List<LinkedHashMap<String, String>> rawTracks = (List<LinkedHashMap<String, String>>) trackMatches.get("track");
        List<TrackSearchDTO> tracks = new ArrayList<>();
        for (LinkedHashMap<String, String> rawTrack : rawTracks) {
            String resTitle = rawTrack.get("name");
            ArtistDTO resArtist = ArtistDTO.builder()
                    .artistName(rawTrack.get("artist"))
                    .build();

            tracks.add(TrackSearchDTO.builder()
                    .title(resTitle)
                    .artistDTO(resArtist)
                    .build());
        }

        return tracks;
    }

    /**
     * Track 의 세부정보를 리턴합니다
     *
     * @param trackName
     * @param artistName
     * @return TrackInfoDTO
     */
    public static TrackInfoDTO getTrack(String trackName, String artistName) {
        Map result = SearchEndpoint.getTrackInfo(trackName, artistName);
        Map rawTrack = (Map) result.get("track");
        String title = (String) rawTrack.get("name");
        ArtistDTO artist = ArtistDTO.builder()
                .artistName(((LinkedHashMap<String, String>) rawTrack.get("artist")).get("name"))
                .build();
        AlbumDTO album = AlbumDTO.builder()
                .name(((LinkedHashMap<String, String>) rawTrack.get("album")).get("title"))
                .build();

        List<LinkedHashMap<String, String>> rawTags = (List<LinkedHashMap<String, String>>)
                ((LinkedHashMap) rawTrack.get("toptags")).get("tag");

        List<TagDTO> tags = rawTags.stream()
                .map(m -> TagDTO.builder()
                        .tagName(m.get("name"))
                        .build())
                .toList();

        return TrackInfoDTO.builder()
                .title(title)
                .artistDTO(artist)
                .albumDTO(album)
                .tags(tags)
                .build();
    }

    /**
     * Track 의 상위 Tag 목록을 리턴합니다
     *
     * @param trackName
     * @param artistName
     * @return List<TagDTO> TagDTO 목록
     */
    public static List<TagDTO> getTrackTags(String trackName, String artistName) {
        Map result = SearchEndpoint.getTrackTopTags(trackName, artistName);
        Map topTags = (Map) result.get("toptags");
        List<LinkedHashMap<String, String>> rawTags = (List<LinkedHashMap<String, String>>) topTags.get("tag");

        return rawTags.stream()
                .map(s -> TagDTO.builder()
                        .tagName(s.get("name"))
                        .build()
                )
                .toList();
    }

    /**
     * Tag 의 상위 Track 목록을 리턴합니다
     *
     * @param tagName
     * @return List<TrackSearchDTO> Track 목록
     */
    public static List<TrackSearchDTO> getTrackFromTag(String tagName) {
        Map result = SearchEndpoint.getTracksFromTag(tagName);
        Map obj = (Map) result.get("tracks");
        List<LinkedHashMap> rawTracks = (ArrayList) obj.get("track");
        List<TrackSearchDTO> tracks = new ArrayList<>();
        for (LinkedHashMap rawTrack : rawTracks) {
            String title = (String) rawTrack.get("name");
            ArtistDTO artist = ArtistDTO.builder()
                    .artistName(((Map<String, String>) rawTrack.get("artist")).get("name"))
                    .build();
            TrackSearchDTO track = TrackSearchDTO.builder()
                    .title(title)
                    .artistDTO(artist)
                    .build();

            tracks.add(track);
        }

        return tracks;
    }
}