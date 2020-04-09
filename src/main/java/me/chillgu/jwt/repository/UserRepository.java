package me.chillgu.jwt.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import me.chillgu.jwt.security.CustomUserDetails;

public interface UserRepository extends JpaRepository<CustomUserDetails, String>{
	
	Optional<CustomUserDetails> findByUsername(String username);
	
}
