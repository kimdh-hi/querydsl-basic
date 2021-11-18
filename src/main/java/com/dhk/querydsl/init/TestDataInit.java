package com.dhk.querydsl.init;

import com.dhk.querydsl.domain.Team;
import com.dhk.querydsl.domain.User;
import com.dhk.querydsl.repository.TeamRepository;
import com.dhk.querydsl.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class TestDataInit implements ApplicationRunner {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Team team1 = new Team("개발팀", 5);
        teamRepository.save(team1);
        Team team2 = new Team("디자인팀", 3);
        teamRepository.save(team2);
        Team team3 = new Team("마케팅팀", 6);
        teamRepository.save(team3);

        List<User> users = new ArrayList<>();
        users.add(createUser("kim", "!234", 30, team1));
        users.add(createUser("lee", "!234", 40, team1));
        users.add(createUser("park", "!234", 25, team1));
        users.add(createUser("jeon", "!234", 18, team2));
        users.add(createUser("hwang", "!234", 35, team2));
        users.add(createUser("kwon", "!234", 28, team3));
        users.add(createUser("oh", "!234", 26, team3));
        userRepository.saveAll(users);
    }

    private User createUser(String username, String password, int age, Team team) {
        return new User(username, password, age, team);
    }
}
