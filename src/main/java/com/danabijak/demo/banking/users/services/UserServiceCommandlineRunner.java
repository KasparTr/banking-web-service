package com.danabijak.demo.banking.users.services;

import java.util.Arrays;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.danabijak.demo.banking.entity.Role;
import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.infra.repositories.UserRepository;

@Component
public class UserServiceCommandlineRunner implements CommandLineRunner{
	
	private static final Logger log =  LoggerFactory.logger(UserServiceCommandlineRunner.class);
	
	@Autowired
	private UserService userService;
	
	@Override
	public void run(String... args) throws Exception {
		createAdminUser();
		createBankEntity();
	}
	
	private void createAdminUser() {
		try {
			userService.insertAdmin(new User(
		              "admin@bank.com",
		              "admin123@ASDF"));
		}catch(Exception e) {
			log.error("Default Admin User not created: " + e.getMessage());
		}
		
	}
	
	private void createBankEntity() {
		try {
			userService.insertBankEntity(new User(
		              "bankItself@bank.com",
		              "bankItSElf123@sad"));
		}catch(Exception e) {
			log.error("Bank User not created: " + e.getMessage());
		}
		
	}
}
