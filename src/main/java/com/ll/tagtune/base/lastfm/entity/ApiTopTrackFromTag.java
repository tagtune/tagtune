package com.ll.tagtune.base.lastfm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiTopTrackFromTag {
    public List<TrackSearchDTO> getTracks() {
        return result.tracks.stream()
//                .sorted(Comparator.comparingLong(t -> t.attr.rank != null ? t.attr.rank : 0))
                .map(t -> TrackSearchDTO.builder()
                        .name(t.name)
                        .artist(t.artist.name)
                        .build()
                )
                .toList();
    }
    @JsonProperty("tracks")
    public Result result;

    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {

        @JsonProperty("track")
        List<Track> tracks = new ArrayList<>();

        @JsonProperty("@attr")
        public Attr attr;

        @ToString
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Track {
            public String name;
            public Artist artist;
            @ToString.Exclude
            public String url;
            @ToString.Exclude
            public List<Image> image = new ArrayList<>();
            @JsonProperty("@attr")
            public Attr attr;

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Artist {
                public String name;
                @ToString.Exclude
                public String url;
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Image {
                @JsonProperty("#text")
                public String url;
                public String size;
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Attr {
                public Long rank;
            }
        }

        @ToString
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Attr {
            public String tag;
            public Long page;
            public Long perPage;
            public Long totalPages;
            public Long total;
        }
    }
}