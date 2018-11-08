package com.danabijak.demo.banking.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.danabijak.demo.banking.entity.Role;
import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.exceptions.UserSavingException;
import com.danabijak.demo.banking.services.UserService;

@RestController
public class UserController {
	
	@PostMapping(value="/register")
	public ResponseEntity<User> createNewUser(@RequestBody User newUser) {
		System.out.println("C_USER | createNewUser()");
		
		User user = attemptNewUserSave(newUser);
		//if (user == null)
			//return ResponseEntity.noContent().build();

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
				"/{id}").buildAndExpand(user.getId()).toUri();

		return ResponseEntity.created(location).build();
		
	}
	
	private User attemptNewUserSave(User newUser) {
		try {
			UserService uService = new UserService();

			return uService.save(new User(
					newUser.getUsername(),
					newUser.getPassword(),
	                Arrays.asList(new Role("USER"), new Role("ACTUATOR")), 
	                true
	        ));
		}catch(Exception use) {
			throw new UserSavingException(use.getLocalizedMessage());
		}
		
	}
}
