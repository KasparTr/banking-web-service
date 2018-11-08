package com.danabijak.demo.banking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.exceptions.UserSavingException;
import com.danabijak.demo.banking.repositories.UserDAOService;
import com.danabijak.demo.banking.repositories.UserRepository;

@Service
public class UserService {
    
    
	private UserDAOService userDaoService = new UserDAOService();

    @Autowired
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public User save(User user) {
		System.out.println("UserService | passwordEncoder: " + passwordEncoder);
		System.out.println("UserService | repo: " + userDaoService);

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userDaoService.insert(user);
        return user;
    }

}
