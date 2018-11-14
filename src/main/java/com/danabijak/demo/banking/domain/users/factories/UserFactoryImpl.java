package com.danabijak.demo.banking.domain.users.factories;

import java.math.BigDecimal;
import java.util.Arrays;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.danabijak.demo.banking.domain.accounts.entity.BankAccount;
import com.danabijak.demo.banking.domain.users.entity.Role;
import com.danabijak.demo.banking.domain.users.entity.User;
import com.danabijak.demo.banking.domain.users.valueobjects.UserRequest;

@Component
public class UserFactoryImpl implements UserFactory {
	
	private static final Logger log =  LoggerFactory.logger(UserFactoryImpl.class);

	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	/*
	 * Allows to create a default banking user that gets a bank account attached with default starting balance amount.
	 */
	@Override
	public User makeDefaultBankingUser(UserRequest userRequest) {
		User user = new User(userRequest.username, passwordEncoder.encode(userRequest.password));
		user.setRoles(Arrays.asList(new Role(Role.NAME.USER), new Role(Role.NAME.ACTUATOR)));
		user.setActive(true);
		user.setName(userRequest.username);
		attachBankAccountToUser(user, BankAccount.DEFAULT_LIMITS.BANKING_USER_START_BALANCE);
		
		return user;
	}

	@Override
	public User makeAdminUser(UserRequest userRequest) {
		User user = new User(userRequest.username, passwordEncoder.encode(userRequest.password));
		user.setRoles(Arrays.asList(new Role(Role.NAME.USER), new Role(Role.NAME.ACTUATOR), new Role(Role.NAME.ADMIN)));
		user.setActive(true);
		user.setName(userRequest.username);
		return user;
	}
	
	@Override
	public User makeBankEntity(UserRequest userRequest) {
		User user = new User(userRequest.username, passwordEncoder.encode(userRequest.password));
		user.setRoles(Arrays.asList(new Role(Role.NAME.USER), new Role(Role.NAME.ACTUATOR), new Role(Role.NAME.BANK)));
		user.setActive(true);
		user.setName(user.getUsername());
		attachBankAccountToUser(user, BankAccount.DEFAULT_LIMITS.MAX_TOTAL_BALANCE);
		
		return user;
	}
	
	private void attachBankAccountToUser(User user, BigDecimal startAmount) {
		try {
			BankAccount ba = new BankAccount(BankAccount.DEFAULT_CURRENCY.USD, user.getUsername());
			ba.setBalance(startAmount);
			user.attachBankAccount(ba);
		}catch(Exception e) {
			log.error("Default bank account could not be attachet to user. Error: " + e.getMessage());
		}
		
		
	}

	

}
