package com.citco.sprinBootDemo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.citco.sprinBootDemo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);

	boolean existsByAccountNumber(String accountNumber);

	User findByAccountNumber(String accountNumber);

}
