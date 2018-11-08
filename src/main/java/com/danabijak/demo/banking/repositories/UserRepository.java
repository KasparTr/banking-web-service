package com.danabijak.demo.banking.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.danabijak.demo.banking.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	 Optional<User> findByUsername(String username);
	 
}
