package com.danabijak.demo.banking.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.passay.DigitCharacterRule;
import org.passay.LengthRule;
import org.passay.LowercaseCharacterRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.SpecialCharacterRule;
import org.passay.UppercaseCharacterRule;
import org.passay.WhitespaceRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.danabijak.demo.banking.entity.Role;
import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.exceptions.UserNotFoundException;
import com.danabijak.demo.banking.exceptions.UserObjectNotValidException;
import com.danabijak.demo.banking.exceptions.UserSavingException;
import com.danabijak.demo.banking.model.UserValidationReport;
import com.danabijak.demo.banking.repositories.UserRepository;

@Component
public class UserService {
    
	
	private static final Logger log =  LoggerFactory.logger(UserDAOServiceCommandlineRunner.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ValidatorService uvs;

	
	public User insertActive(User user) {
		UserValidationReport uvr = uvs.validateClientSentUser(user);
		if(uvr.valid) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setRoles(Arrays.asList(new Role(Role.NAME.USER), new Role(Role.NAME.ACTUATOR)));
			user.setActive(true);

			userRepository.save(user);
			
			log.info("New User was created: " + user.getId());
			return user;
		}
		else
			throw new UserObjectNotValidException("User Object Not Valid. Errors: " + uvr.generateStringMessage());
    }
	
	public User insertAdmin(User user) {
		UserValidationReport uvr = uvs.validateClientSentUser(user);
		if(uvr.valid) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setRoles(Arrays.asList(new Role(Role.NAME.USER), new Role(Role.NAME.ACTUATOR), new Role(Role.NAME.ADMIN)));
			user.setActive(true);

			userRepository.save(user);
			
			log.info("New User was created: " + user.getId());
			return user;
		}
		else
			throw new UserObjectNotValidException("User Object Not Valid. Errors: " + uvr.generateStringMessage());

    }
	
	public User find(long id) {
		Optional<User> user = userRepository.findById(id);
		
		if(user.isPresent())
			return user.get();
		else 
			throw new UserNotFoundException(String.format("User with ID %s not found", id));
    }
	
	public List<User> getAll() {
		List<User> users = userRepository.findAll();

		if(users.isEmpty())
			throw new UserNotFoundException("No Users Found");
		else 
			return users;
    }
	


}
