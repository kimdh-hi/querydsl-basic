package com.dhk.querydsl.controller;

import com.dhk.querydsl.dto.UserDto;
import com.dhk.querydsl.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/user/team/{teamName}/senior")
    public ResponseEntity<List<UserDto>> test(@PathVariable String teamName) {
        List<UserDto> userDtos = userRepository.findSeniorUserByTeam(teamName);
        return ResponseEntity.ok(userDtos);
    }
}
