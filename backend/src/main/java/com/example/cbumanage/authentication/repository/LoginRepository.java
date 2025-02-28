package com.example.cbumanage.authentication.repository;

import com.example.cbumanage.authentication.entity.LoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<LoginEntity, Long> {
	Optional<LoginEntity> findByEmailEquals(String email);

	Optional<LoginEntity> findBystudentNumber(Long studentNumber);
}
