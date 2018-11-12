package com.danabijak.demo.banking;

import java.math.BigDecimal;

import com.danabijak.demo.banking.entity.BankAccount;
import com.danabijak.demo.banking.entity.User;

public class GlobalMethodsForTesting {
	public static String VALID_USERNAME_EXAMPLE = "test@email.com";
	public static String VALID_PASSWORD_EXAMPLE = "pAS24@a3asd2KSH";
	
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

	
}
