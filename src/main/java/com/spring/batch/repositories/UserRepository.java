package com.spring.batch.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.batch.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
