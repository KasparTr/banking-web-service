package com.danabijak.demo.banking.transactions.model;

import java.util.List;

import com.danabijak.demo.banking.entity.Transaction;

public class AccountTransactions {
	public final List<Transaction> debitTransactions;
	public final List<Transaction> creditTransactions;
	public AccountTransactions(List<Transaction> debitTransactions, List<Transaction> creditTransactions) {
		super();
		this.debitTransactions = debitTransactions;
		this.creditTransactions = creditTransactions;
	}
	
	

}
