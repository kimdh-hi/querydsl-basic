package com.dhk.querydsl;

import com.dhk.querydsl.domain.QUser;
import com.dhk.querydsl.domain.Team;
import com.dhk.querydsl.domain.User;
import com.dhk.querydsl.repository.TeamRepository;
import com.dhk.querydsl.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Transactional
@SpringBootTest
public class QueryDslTest {

    @Autowired private UserRepository userRepository;
    @Autowired private TeamRepository teamRepository;
    @Autowired private JPAQueryFactory query;

    @BeforeEach
    void before() {
        Team team1 = new Team("team1");
        teamRepository.save(team1);

        User user1 = new User("test1", "test1");
        user1.setTeam(team1);
        User user2 = new User("test2", "test2");
        user2.setTeam(team1);

        userRepository.saveAll(Arrays.asList(user1, user2));
    }

    @DisplayName("1. 기본조회 조건X")
    @Test
    void 기본조회() {
        // select * from user;
        List<User> users = query.selectFrom(QUser.user)
                .fetch();

        users.forEach(u -> System.out.println(u.getUsername()));
    }


}
