package com.dhk.querydsl.repository.dao;

import com.dhk.querydsl.domain.User;
import com.dhk.querydsl.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {

    // 어떤 Team에 속한 User 중 나이가 30대인 User
    List<UserDto> findSeniorUserByTeam(String teamName);
}
