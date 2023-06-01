package com.ll.tagtune.base.lastfm;

import com.ll.tagtune.boundedContext.track.entity.Track;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.ll.tagtune.base.lastfm.SearchEndpoint.getTrackInfo;
import static com.ll.tagtune.base.lastfm.SearchEndpoint.searchTrack;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResultParser {
    /**
     * 검색 결과를 Track 으로 파싱하여 리턴합니다
     *
     * @param query 검색어
     * @return List<Track>
     */
    public static List<Track> searchTracks(String query) {
        Map obj = searchTrack(query);
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
     * 검색 결과를 Track 으로 파싱하여 리턴합니다
     * <p>
     * {track={name=IU, url=https://www.last.fm/music/Blueming/_/IU, duration=0, streamable={#text=0, fulltrack=0}, listeners=16, playcount=39, artist={name=Blueming, url=https://www.last.fm/music/Blueming}, toptags={tag=[]}}}
     *
     * @return List<Track>
     */
    public static Track getTrack(String trackName, String artistName) {
        Map rawTrack = getTrackInfo(trackName, artistName);
        return Track.builder()
                .title((String) rawTrack.get("name"))
                .artist((String) rawTrack.get("artist"))
                .build();
    }
}
