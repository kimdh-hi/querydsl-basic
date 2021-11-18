package com.dhk.querydsl.repository;

import com.dhk.querydsl.domain.User;
import com.dhk.querydsl.repository.dao.UserDao;
import com.dhk.querydsl.repository.dao.UserDaoImpl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserDao {

    Optional<User> findByUsername(String username);
}
