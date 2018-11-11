package com.danabijak.demo.banking.users.factories;

import java.math.BigDecimal;
import java.util.Arrays;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.danabijak.demo.banking.entity.Balance;
import com.danabijak.demo.banking.entity.BankAccount;
import com.danabijak.demo.banking.entity.Role;
import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.transactions.exceptions.BankAccountException;
import com.danabijak.demo.banking.users.services.UserService;

@Component
public class UserFactoryImpl implements UserFactory {
	
	private static final Logger log =  LoggerFactory.logger(UserFactoryImpl.class);

	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	

	/*
	 * Allows to create a default banking user that gets a bank account attached with default starting balance amount.
	 */
	@Override
	public User makeDefaultBankingUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRoles(Arrays.asList(new Role(Role.NAME.USER), new Role(Role.NAME.ACTUATOR)));
		user.setActive(true);
		user.setName(user.getUsername());
		attachBankAccountToUser(user, Balance.DEFAULT_LIMITS.BANKING_USER_START_BALANCE);
		
		return user;
	}

	@Override
	public User makeAdminUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRoles(Arrays.asList(new Role(Role.NAME.USER), new Role(Role.NAME.ACTUATOR), new Role(Role.NAME.ADMIN)));
		user.setActive(true);
		user.setName(user.getUsername());
		return user;
	}
	
	@Override
	public User makeBankEntity(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRoles(Arrays.asList(new Role(Role.NAME.USER), new Role(Role.NAME.ACTUATOR), new Role(Role.NAME.BANK)));
		user.setActive(true);
		user.setName(user.getUsername());
		attachBankAccountToUser(user, Balance.DEFAULT_LIMITS.MAX_TOTAL_BALANCE);
		
		return user;
	}
	
	private void attachBankAccountToUser(User user, BigDecimal startAmount) {
		try {
			BankAccount ba = new BankAccount(BankAccount.DEFAULT_CURRENCY.USD, user.getUsername());
			ba.getBalance().setTotalAmount(startAmount);
			user.attachBankAccount(ba);
		}catch(Exception e) {
			log.error("Default bank account could not be attachet to user. Error: " + e.getMessage());
		}
		
		
	}

	

}
