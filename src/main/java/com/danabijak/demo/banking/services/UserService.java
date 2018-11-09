package com.danabijak.demo.banking.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.danabijak.demo.banking.entity.Role;
import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.exceptions.UserNotFoundException;
import com.danabijak.demo.banking.exceptions.UserSavingException;
import com.danabijak.demo.banking.repositories.UserRepository;

@Component
public class UserService {
    
	
	private static final Logger log =  LoggerFactory.logger(UserDAOServiceCommandlineRunner.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public User insertActive(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRoles(Arrays.asList(new Role(Role.NAME.USER), new Role(Role.NAME.ACTUATOR)));
		user.setActive(true);

		userRepository.save(user);
		
		log.info("New User was created: " + user.getId());
        
		return user;
    }
	
	public User insertAdmin(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRoles(Arrays.asList(new Role(Role.NAME.USER), new Role(Role.NAME.ACTUATOR), new Role(Role.NAME.ADMIN)));
		user.setActive(true);

		userRepository.save(user);
		
		log.info("New Admin was created: " + user.getId());
        
		return user;
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
