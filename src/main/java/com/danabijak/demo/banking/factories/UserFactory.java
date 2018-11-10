package com.danabijak.demo.banking.factories;

import org.springframework.stereotype.Component;

import com.danabijak.demo.banking.entity.User;

@Component
public interface UserFactory {
	/*
	 * Changes (makes) the user into a valid transactional banking user
	 */
	public User makeDefaultBankingUser(User user);
	
	/*
	 * Changes (makes) the user into a valid admin user
	 */
	public User makeAdminUser(User user);
	
	/*
	 * Changes (makes) the user into a valid admin user
	 */
	public User makeBankEntity(User user);
	
}
