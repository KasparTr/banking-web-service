package com.danabijak.demo.banking.services;

import java.util.Arrays;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.danabijak.demo.banking.entity.Role;
import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.repositories.UserRepository;

@Component
public class UserDAOServiceCommandlineRunner implements CommandLineRunner{
	
//	private static final Logger log =  LoggerFactory.logger(UserDAOServiceCommandlineRunner.class);
//	
//	@Autowired
//	private UserRepository userService;
//	
//	@Autowired
//	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
//		User user = new User(
//              "admin",
//              passwordEncoder.encode("admin"),
//              Arrays.asList(new Role("USER"), new Role("ACTUATOR")),//roles 
//              true);
//		userService.save(user);
//		log.info("New User is created: " + user.getId());
	}
}
