package com.danabijak.demo.banking.users.factories;

import org.springframework.stereotype.Component;

import com.danabijak.demo.banking.users.entity.User;

@Component
public interface UserFactory {
	/**
	 * Changes (makes) the user into a valid transactional entity - banking user
	 */
	public User makeDefaultBankingUser(User user);
	
	/**
	 * Changes (makes) the user into a valid admin user - banking user
	 */
	public User makeAdminUser(User user);
	
	/**
	 * Changes (makes) the user into a bank entity
	 */
	public User makeBankEntity(User user);
	
}
