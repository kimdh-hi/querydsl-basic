package com.dhk.querydsl.repository.dao;

import com.dhk.querydsl.domain.QTeam;
import com.dhk.querydsl.domain.QUser;
import com.dhk.querydsl.domain.User;
import com.dhk.querydsl.dto.QUserDto;
import com.dhk.querydsl.dto.UserDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

import static com.dhk.querydsl.domain.QTeam.*;
import static com.dhk.querydsl.domain.QUser.user;

@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final JPAQueryFactory query;

    @Override
    public List<UserDto> findSeniorUserByTeam(String teamName) {
        return query
                .select(new QUserDto(user.username, user.age, team.teamName))
                .from(user)
                .join(user.team, team)
                .where(teamNameEquals(teamName), userAgeGreaterOrEqualsThan(30))
                .fetch();
    }

    private BooleanExpression teamNameEquals(String teamName) {
        return Objects.isNull(teamName) ? null : team.teamName.eq(teamName);
    }

    private BooleanExpression userAgeGreaterOrEqualsThan(int age) {
        return age < 1 ? null : user.age.goe(age);
    }
}
