package com.danabijak.demo.banking.transactions.http;

import java.math.BigDecimal;

import org.joda.money.Money;

import com.danabijak.demo.banking.entity.BankAccount;
import com.danabijak.demo.banking.entity.User;

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
