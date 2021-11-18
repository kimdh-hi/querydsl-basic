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
    private String teamName;

    private int teamAge; // 팀이 생긴지 몇 년 됐는지... 테스트를 위해 억지로 ..ㅎ

    public Team(String teamName, int teamAge) {
        this.teamName = teamName;
        this.teamAge = teamAge;
    }
}
