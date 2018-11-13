package com.danabijak.demo.banking.accounts.http;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.danabijak.demo.banking.accounts.entity.BankAccount;
import com.danabijak.demo.banking.transactions.entity.Transaction;
import com.danabijak.demo.banking.transactions.http.TransactionClientResponse;

public class AccountStatementClientResponse {
	public final long bankAccountId;
	public final BigDecimal totalBalance;
	public final String name;
	public final List<TransactionClientResponse> transactions;
	public final Date createdAt;
	
	public AccountStatementClientResponse(
			BankAccount account,
			List<TransactionClientResponse> transactions,
			BigDecimal totalBalance) {
		super();
		this.bankAccountId = account.getId();
		this.name = account.getName();
		this.transactions = transactions;
		this.createdAt = new Date();
		this.totalBalance = totalBalance;
	}
}
