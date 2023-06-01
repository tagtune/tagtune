package com.ll.tagtune.base.lastfm;

import com.ll.tagtune.boundedContext.tag.entity.Tag;
import com.ll.tagtune.boundedContext.track.entity.Track;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResultParser {
    /**
     * 검색 결과를 Track 으로 파싱하여 리턴합니다
     *
     * @param query 검색어
     * @return List<Track> 트랙 목록
     */
    public static List<Track> searchTracks(String query) {
        Map obj = SearchEndpoint.searchTrack(query);
        Map results = (Map) obj.get("results");
        LinkedHashMap trackMatches = (LinkedHashMap) results.get("trackmatches");
        List<LinkedHashMap<String, String>> rawTracks = (List<LinkedHashMap<String, String>>) trackMatches.get("track");
        return rawTracks
                .stream()
                .map(o -> Track.builder()
                        .title(o.get("name"))
                        .artist(o.get("artist"))
                        .build()
                ).toList();
    }

    /**
     * Track 의 세부정보를 리턴합니다
     *
     * @param trackName
     * @param artistName
     * @return Track
     */
    public static Track getTrack(String trackName, String artistName) {
        Map result = SearchEndpoint.getTrackInfo(trackName, artistName);
        Map rawTrack = (Map) result.get("track");

        return Track.builder()
                .title((String) rawTrack.get("name"))
                .artist(((LinkedHashMap<String, String>) rawTrack.get("artist")).get("name"))
                .build();
    }

    /**
     * Track 의 상위 Tag 목록을 리턴합니다
     *
     * @param trackName
     * @param artistName
     * @return List<Tag> Tag 목록
     */
    public static List<Tag> getTrackTags(String trackName, String artistName) {
        Map result = SearchEndpoint.getTrackTopTags(trackName, artistName);
        Map topTags = (Map) result.get("toptags");
        List<LinkedHashMap<String, String>> rawTags = (List<LinkedHashMap<String, String>>) topTags.get("tag");

        return rawTags.stream()
                .map(s -> Tag.builder()
                        .tagName(s.get("name"))
                        .build()
                )
                .collect(Collectors.toList());
    }

    /**
     * Tag 의 상위 Track 목록을 리턴합니다
     *
     * @param tagName
     * @return List<Track> Track 목록
     */
    public static List<Track> getTrackFromTag(String tagName) {
        Map result = SearchEndpoint.getTracksFromTag(tagName);
        Map tracks = (Map) result.get("tracks");
        List<LinkedHashMap> rawTracks = (ArrayList) tracks.get("track");

        return rawTracks.stream()
                .map(m -> Track.builder()
                        .title((String) m.get("name"))
                        .artist(((Map<String, String>) m.get("artist")).get("name"))
                        .build())
                .toList();
    }
}
