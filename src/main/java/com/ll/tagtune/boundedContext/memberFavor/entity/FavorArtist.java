package com.ll.tagtune.boundedContext.memberFavor.entity;

import com.ll.tagtune.boundedContext.artist.entity.Artist;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class FavorArtist extends FavorBase<Artist> {
}
