package com.danabijak.demo.banking.domain.accounts.valueobjects;

import java.math.BigDecimal;

import com.danabijak.demo.banking.domain.accounts.entity.BankAccount;

public class AccountBalanceResponse {
	public final String accountName;
	public final long accountId;
	public final BigDecimal balance;
	public final String currency;
	
	public AccountBalanceResponse(BankAccount account) {
		super();
		this.accountName = account.getName();
		this.accountId = account.getId();
		this.balance = account.getBalance().getAmount();
		this.currency =account.getBalance().getCurrencyUnit().toString();
	}
	
	
	

}
