package com.dhk.querydsl.repository;

import com.dhk.querydsl.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
