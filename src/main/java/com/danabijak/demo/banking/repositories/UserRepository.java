package com.danabijak.demo.banking.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.danabijak.demo.banking.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
		 Optional<User> findByUsername(String username);
}
