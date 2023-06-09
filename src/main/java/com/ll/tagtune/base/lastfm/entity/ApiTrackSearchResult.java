package com.ll.tagtune.base.lastfm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiTrackSearchResult {
    public List<TrackSearchDTO> getTracks() {
        return results.trackmatches.track.stream()
                .map(t -> TrackSearchDTO.builder()
                        .name(t.name)
                        .artist(t.artist)
                        .build()
                ).toList();
    }

    public Results results;

    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Results {
        @JsonProperty("opensearch:Query")
        @ToString.Exclude
        public OpensearchQuery opensearchQuery;
        @JsonProperty("opensearch:totalResults")
        @ToString.Exclude
        public Long opensearchTotalResults;
        @JsonProperty("opensearch:startIndex")
        @ToString.Exclude
        public Long opensearchStartIndex;
        @JsonProperty("opensearch:itemsPerPage")
        @ToString.Exclude
        public Long opensearchItemsPerPage;
        @JsonProperty("trackmatches")
        @ToString.Exclude
        public TrackMatches trackmatches;

        @ToString
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class TrackMatches {
            public List<Track> track = new ArrayList<>();

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Track {
                public String name;
                public String artist;
                @ToString.Exclude
                public String url;
                public String listeners;
                @ToString.Exclude
                public List<Image> image = new ArrayList<>();

                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Image {
                    @JsonProperty("#text")
                    public String url;
                    public String size;
                }
            }
        }

        @ToString
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class OpensearchQuery {
            public String role;
            public Long startPage;
        }
    }
}
