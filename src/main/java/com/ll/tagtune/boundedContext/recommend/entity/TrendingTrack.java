package com.ll.tagtune.boundedContext.recommend.entity;

import com.ll.tagtune.base.lastfm.entity.TrackSearchDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.stream.IntStream;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrendingTrack {
    @Id
    private Integer id;
    private Integer trackRank;
    private String title;
    private String artist;

    public static List<TrendingTrack> of(List<TrackSearchDTO> searchDTOs) {
        return IntStream.range(0, searchDTOs.size())
                .mapToObj(i -> new TrendingTrack(
                        i + 1,
                        i + 1,
                        searchDTOs.get(i).getName(),
                        searchDTOs.get(i).getArtist()
                ))
                .toList();
    }
}
