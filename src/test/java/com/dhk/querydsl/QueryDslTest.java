package com.dhk.querydsl;

import com.dhk.querydsl.domain.QTeam;
import com.dhk.querydsl.domain.QUser;
import com.dhk.querydsl.domain.Team;
import com.dhk.querydsl.domain.User;
import com.dhk.querydsl.dto.QUserDto;
import com.dhk.querydsl.dto.UserDto;
import com.dhk.querydsl.repository.TeamRepository;
import com.dhk.querydsl.repository.UserRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.*;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.management.Query;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.dhk.querydsl.domain.QTeam.*;
import static com.dhk.querydsl.domain.QUser.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@SpringBootTest
public class QueryDslTest {

    @Autowired private UserRepository userRepository;
    @Autowired private TeamRepository teamRepository;
    @Autowired private JPAQueryFactory query;

    @BeforeEach
    void before() {
        Team team1 = new Team("team1", 10);
        teamRepository.save(team1);
        Team team2 = new Team("team2", 15);
        teamRepository.save(team2);
        Team team3 = new Team("team3", 5);
        teamRepository.save(team3);

        User user1 = new User("user1", "test1", 20);
        user1.setTeam(team1);
        User user2 = new User("user2", "test2", 30);
        user2.setTeam(team1);

        User user3 = new User("user3", "test3", 29);
        user3.setTeam(team2);
        User user4 = new User("user4", "test4", 40);
        user4.setTeam(team3);
        User user5 = new User("user5", "test5", 20);
        User user6 = new User("user6", "test6", 50);

        userRepository.saveAll(Arrays.asList(user1, user2, user3, user4, user5, user6));
    }

    @Order(1)
    @DisplayName("1. 기본조회 조건없이 모두 조회")
    @Test
    void 기본조회() {
        // select * from user;
        List<User> users = query.selectFrom(user)
                .fetch();

        users.forEach(u -> System.out.println(u.getUsername()));
    }

    @Order(2)
    @Test
    @DisplayName("2. 기본조회 where 절 포함")
    void 기본조회_where() {
        // select * from user where username = test1;
        List<User> users = query.selectFrom(user)
                .where(user.username.eq("test1"))
                .fetch();

        users.forEach(u -> System.out.println(u.getUsername()));
    }

    @Order(3)
    @Test
    @DisplayName("2-1 기본조회 where절 포힘")
    void 기본조회_where2() {
        // select * from user where age between 20 and 30;
        List<User> users = query.selectFrom(user)
                .where(user.age.between(20, 30))
                .fetch();
        users.forEach(u -> System.out.println(u.getUsername()));
    }

    @Order(4)
    @Test
    @DisplayName("3. 정렬")
    void 나이_정렬_오름차순() {
        // 팀1에 속한 user를 나이순으로 정렬
        /**
         * select * from user u
         * join team t
         * where u.team_id = t.id and t.name = team1
         * orderBy u.age asc;
          */
        List<User> users = query.selectFrom(QUser.user)
                .where(user.team.teamName.eq("team1"))
                .orderBy(QUser.user.age.asc())
                .fetch();
        users.forEach(u -> System.out.println(u.getAge()));
    }

    @Order(5)
    @Test
    @DisplayName("4. 페이징")
    void 페이징() {
        QueryResults<User> queryResults = query.selectFrom(user)
                .offset(1)
                .limit(3)
                .fetchResults();

        List<User> users = queryResults.getResults();
        for (User user1 : users) {
            System.out.println(user1.getUsername());
        }

        System.out.println("offset = " + queryResults.getOffset());
        System.out.println("limit = " + queryResults.getLimit());
        System.out.println("total = " + queryResults.getTotal());
    }

    @Order(6)
    @Test
    @DisplayName("5. Join")
    void join1() {
        // team, user inner join
        List<User> users = query.selectFrom(user)
                .join(user.team, team)
                .fetch();

        users.forEach(u -> System.out.println(u.getUsername()));

        assertEquals(4, users.size());
    }

    @Order(7)
    @Test
    @DisplayName("6. left join")
    void leftJoin() {
        List<User> users = query.selectFrom(user)
                .leftJoin(user.team, team)
                .fetch();

        users.forEach(u -> System.out.println(u.getUsername()));

        assertEquals(6, users.size());
    }

    @Order(8)
    @Test
    @DisplayName("7. where절 서브쿼리")
    void whereSubquery() {


        List<User> users = query.selectFrom(user)
                .where(user.age.in(
                        JPAExpressions
                                .select(team.teamAge.max())
                                .from(team)
                ))
                .fetch();

        assertEquals(1, users.size());
    }

    @Order(9)
    @Test
    @DisplayName("8. case 쿼리")
    void caseQuery() {

        List<String> results = query
                .select(new CaseBuilder()
                        .when(user.age.loe(30)).then("주니어")
                        .when(user.age.goe(31)).then("시니어")
                        .otherwise("어린이: " + user.age)
                ).from(user)
                .fetch();

        results.forEach(System.out::println);
    }

    @Order(10)
    @Test
    @DisplayName("9. 프로젝션 - DTO의 생성자 사용")
    void 프로젝션_DTO() {

        List<UserDto> userDtos = query.select(new QUserDto(user.username, user.age, team.teamName))
                .from(user)
                .join(user.team, team)
                .fetch();

        userDtos.forEach(ut -> System.out.println(ut.getUsername() + ": "+ ut.getTeamName()));
    }

    @Order(11)
    @Test
    @DisplayName("10. 동적쿼리1")
    void 동적쿼리1() {
        List<UserDto> userDtos = getUserByUsernameAndTeamName("user1", "team1");

        userDtos.forEach(ut -> System.out.println(ut.getUsername() + ": "+ ut.getTeamName()));
        assertEquals(1, userDtos.size());
    }

    private List<UserDto> getUserByUsernameAndTeamName(String username, String teamName) {

        BooleanBuilder builder = new BooleanBuilder();
        if (!Objects.isNull(username)) {
            builder.and(user.username.eq(username));
        }
        if (!Objects.isNull(teamName)) {
            builder.and(team.teamName.eq(teamName));
        }

        return query
                .select(new QUserDto(user.username, user.age, team.teamName))
                .from(user)
                .join(user.team, team)
                .where(builder)
                .fetch();
    }

    @Order(12)
    @Test
    @DisplayName("11. 동적쿼리2 조건 메서드로 빼내기")
    void 동적쿼리2() {

        List<UserDto> userDtos = query
                .select(new QUserDto(user.username, user.age, team.teamName))
                .from(user)
                .join(user.team, team)
                .where(ageGreaterThanEquals(20), teamNameEquals("team3")) // and로 연결
                .fetch();

        assertEquals(1, userDtos.size());
    }

    private BooleanExpression ageGreaterThanEquals(int age) {
        return age <= 0 ? null : user.age.goe(age);
    }

    private BooleanExpression teamNameEquals(String teamName) {
        return Objects.isNull(teamName) ? null : team.teamName.eq(teamName);
    }
}
