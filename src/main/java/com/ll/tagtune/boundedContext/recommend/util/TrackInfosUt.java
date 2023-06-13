package com.ll.tagtune.boundedContext.recommend.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ll.tagtune.boundedContext.track.dto.TrackInfoDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TrackInfosUt {
    public static String serialize(List<TrackInfoDTO> trackList) {
        Gson gson = new Gson();

        return gson.toJson(trackList);
    }

    public static List<TrackInfoDTO> deserialize(String json) {
        Gson gson = new Gson();

        return gson.fromJson(json, new TypeToken<List<TrackInfoDTO>>() {
        }.getType());
    }
}
