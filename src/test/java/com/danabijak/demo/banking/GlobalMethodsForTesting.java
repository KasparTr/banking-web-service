package com.danabijak.demo.banking;

import java.math.BigDecimal;

import com.danabijak.demo.banking.domain.accounts.entity.BankAccount;
import com.danabijak.demo.banking.domain.users.entity.User;
import com.danabijak.demo.banking.domain.users.valueobjects.UserRequest;

public class GlobalMethodsForTesting {
	public static String VALID_USERNAME_EXAMPLE = "test@email.com";
	public static String VALID_PASSWORD_EXAMPLE = "pAS24@a3asd2KSH";
	public static String BANK_USERNAME_EXAMPLE = "bankItself@bank.com";
	
	public static User getDummyUserWithBankAccountSetTo(int i) {
		User user = new User(VALID_USERNAME_EXAMPLE,VALID_PASSWORD_EXAMPLE);

		BankAccount ba = new BankAccount(BankAccount.DEFAULT_CURRENCY.USD, user.getUsername());
		ba.setBalance(new BigDecimal(i));
		user.attachBankAccount(ba);

		return user;
	}
	
	public static User getDummyDefaultUser() {
		User user = new User(VALID_USERNAME_EXAMPLE,VALID_PASSWORD_EXAMPLE);

		BankAccount ba = new BankAccount(BankAccount.DEFAULT_CURRENCY.USD, user.getUsername());
		ba.setBalance(BankAccount.DEFAULT_LIMITS.BANKING_USER_START_BALANCE);
		user.attachBankAccount(ba);

		return user;
	}

	public static User getDummyBankUser() {
		User user = new User(BANK_USERNAME_EXAMPLE, VALID_PASSWORD_EXAMPLE);

		BankAccount ba = new BankAccount(BankAccount.DEFAULT_CURRENCY.USD, user.getUsername());
		ba.setBalance(new BigDecimal(1000000));
		user.attachBankAccount(ba);

		return user;
	}
	
	public static UserRequest getValidUserRequest() {
		return new UserRequest(VALID_USERNAME_EXAMPLE, VALID_PASSWORD_EXAMPLE);
	}

	
}
