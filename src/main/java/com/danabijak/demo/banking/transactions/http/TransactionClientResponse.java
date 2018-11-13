package com.danabijak.demo.banking.transactions.http;

import java.math.BigDecimal;
import java.util.Date;

import com.danabijak.demo.banking.transactions.entity.Transaction;
import com.danabijak.demo.banking.transactions.entity.TransactionIntent;
import com.danabijak.demo.banking.transactions.entity.TransactionIntentStatus;
import com.danabijak.demo.banking.users.http.TransactionalEntityClientR;

public class TransactionClientResponse {
	public enum TRANSACTION_TYPE{
		DEBIT, CREDIT
	}
	
	public final long id;
	public final BigDecimal amount;
	public final String currency;
	public final Date createdAt;
	public final TransactionalEntityClientR beneficiaryAccount;
	public final TransactionalEntityClientR sourceAccount;
	public final TRANSACTION_TYPE type;
	

	public TransactionClientResponse(TRANSACTION_TYPE type, Transaction transaction) {
		this.type = type;
		this.id = transaction.id;
		this.amount = transaction.amount.getAmount();
		this.currency = transaction.amount.getCurrencyUnit().toString();
		this.createdAt = transaction.created;
		
		this.beneficiaryAccount = new TransactionalEntityClientR(
				transaction.beneficiaryAccount.getId(), 
				transaction.beneficiaryAccount.getName());
		
		this.sourceAccount = new TransactionalEntityClientR(
				transaction.sourceAccount.getId(), 
				transaction.sourceAccount.getName());
		
	}

}
