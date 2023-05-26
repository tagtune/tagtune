package com.ll.tagtune.boundedContext.year.entity;

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
public class Year {
    @Id
    @Column(nullable = false, unique = true)
    private Integer yearValue;
}
