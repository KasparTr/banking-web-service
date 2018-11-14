package com.danabijak.demo.banking.domain.users.factories;

import org.springframework.stereotype.Component;

import com.danabijak.demo.banking.domain.users.entity.User;
import com.danabijak.demo.banking.domain.users.valueobjects.UserRequest;

@Component
public interface UserFactory {
	/**
	 * Changes (makes) the user into a valid transactional entity - banking user
	 */
	public User makeDefaultBankingUser(UserRequest user);
	
	/**
	 * Changes (makes) the user into a valid admin user - banking user
	 */
	public User makeAdminUser(UserRequest user);
	
	/**
	 * Changes (makes) the user into a bank entity
	 */
	public User makeBankEntity(UserRequest user);
	
}
