package com.danabijak.demo.banking.domain.accounts.valueobjects;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.danabijak.demo.banking.domain.accounts.entity.BankAccount;
import com.danabijak.demo.banking.domain.transactions.valueobjects.TransactionResponse;

public class AccountStatementResponse {
	public final long bankAccountId;
	public final BigDecimal totalBalance;
	public final String name;
	public final List<TransactionResponse> transactions;
	public final Date createdAt;
	
	public AccountStatementResponse(
			BankAccount account,
			List<TransactionResponse> transactions,
			BigDecimal totalBalance) {
		super();
		this.bankAccountId = account.getId();
		this.name = account.getName();
		this.transactions = transactions;
		this.createdAt = new Date();
		this.totalBalance = totalBalance;
	}
	
	public String toString() {
		return "bankAccountId: " + bankAccountId + ", " + "createdAt: " + createdAt;
	}
}
