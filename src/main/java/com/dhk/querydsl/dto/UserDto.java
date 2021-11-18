package com.dhk.querydsl.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserDto {
    private String username;
    private int age;
    private String teamName;

    @QueryProjection // Q타입 클래스로 만들기 위함
    public UserDto(String username, int age, String teamName) {
        this.username = username;
        this.age = age;
        this.teamName = teamName;
    }
}
