package com.ll.tagtune.boundedContext.ReleaseYear.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class ReleaseYear {
    @Id
    @Column(nullable = false, unique = true)
    private Integer yearValue;
}
