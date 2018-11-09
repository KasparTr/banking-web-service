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
public class UserServiceCommandlineRunner implements CommandLineRunner{
	
	private static final Logger log =  LoggerFactory.logger(UserDAOServiceCommandlineRunner.class);
	
	@Autowired
	private UserService userService;
	
	@Override
	public void run(String... args) throws Exception {
		createAdminUser();
	}
	
	private void createAdminUser() {
		userService.insertAdmin(new User(
	              "admin",
	              "admin"));
	}
}