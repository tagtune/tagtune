package com.ll.tagtune.base.lastfm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiTopTracksFromTrending {
    @JsonProperty("tracks")
    public Tracks results;

    public List<TrackSearchDTO> getTracks() {
        return results.topTracks.stream()
                .map(t -> TrackSearchDTO.builder()
                        .name(t.name)
                        .artist(t.artist.name)
                        .build()
                ).toList();
    }

    public static class Tracks {
        @JsonProperty("track")
        public List<Track> topTracks = new ArrayList<>();
        @JsonProperty("@attr")
        public Attr attr;

        @ToString
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Track {
            public String name;
            public Long playcount;
            public Long listeners;
            @ToString.Exclude
            public String url;
            public Artist artist;

            @ToString.Exclude
            public List<Image> image = new ArrayList<>();

            @ToString
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Artist {
                public String name;
                @ToString.Exclude
                public String mbid;
                @ToString.Exclude
                public String url;
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Image {
                @JsonProperty("#text")
                public String url;
                public String size;
            }
        }

        @ToString
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Attr {
            public String page;
            public String perPage;
            public String totalPages;
            public String total;
        }
    }
}