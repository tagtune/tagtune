package com.ll.tagtune.base.lastfm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiTrackInfoResult {
    public Track track;
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Track {
        public String name;
        @ToString.Exclude
        public String mbid;
        @ToString.Exclude
        public String url;
        public String duration;
        public String listeners;
        public String playcount;
        public Artist artist;
        public Album album;
        public Toptags toptags;
        @ToString.Exclude
        public Wiki wiki;
    }

    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Artist {
        public String name;
        @ToString.Exclude
        public String mbid;
        @ToString.Exclude
        public String url;
    }

    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Album {
        public String artist;
        public String title;
        @ToString.Exclude
        public String mbid;
        @ToString.Exclude
        public String url;
        @ToString.Exclude
        public List<Image> image = new ArrayList<>();

        @ToString
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Image {
            @JsonProperty("#text")
            public String url;
            public String size;
        }
    }

    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Toptags {
        @JsonProperty("tag")
        public List<Tag> tags = new ArrayList<>();

        @ToString
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Tag {
            public String name;
            @ToString.Exclude
            public String url;
        }
    }

    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Wiki {
        public String published;
        public String summary;
        public String content;
    }
}
