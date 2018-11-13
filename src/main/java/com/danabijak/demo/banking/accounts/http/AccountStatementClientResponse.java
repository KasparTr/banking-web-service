package com.danabijak.demo.banking.accounts.http;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.danabijak.demo.banking.entity.Transaction;
import com.danabijak.demo.banking.transactions.http.TransactionClientResponse;

public class AccountStatementClientResponse {
	public final long bankAccountId;
	public final BigDecimal totalBalance;
	public final String user;
	public final List<TransactionClientResponse> transactions;
	public final Date createdAt;
	
	public AccountStatementClientResponse(
			long bankAccountId,
			String user,
			List<TransactionClientResponse> transactions,
			BigDecimal totalBalance) {
		super();
		this.bankAccountId = bankAccountId;
		this.user = user;
		this.transactions = transactions;
		this.createdAt = new Date();
		this.totalBalance = totalBalance;
	}
}
