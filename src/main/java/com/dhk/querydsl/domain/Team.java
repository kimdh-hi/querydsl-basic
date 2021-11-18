package com.dhk.querydsl.domain;

import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
public class Team {

    @Id @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String name;

    public Team(String name) {
        this.name = name;
    }
}
