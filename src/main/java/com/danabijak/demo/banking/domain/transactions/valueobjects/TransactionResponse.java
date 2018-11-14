package com.danabijak.demo.banking.domain.transactions.valueobjects;

import java.math.BigDecimal;
import java.util.Date;

import com.danabijak.demo.banking.domain.transactions.entity.Transaction;
import com.danabijak.demo.banking.domain.users.valueobjects.TransactionalEntityRR;

public class TransactionResponse {
	public enum TRANSACTION_TYPE{
		DEBIT, CREDIT
	}
	
	public final long id;
	public final BigDecimal amount;
	public final String currency;
	public final Date createdAt;
	public final TransactionalEntityRR beneficiaryAccount;
	public final TransactionalEntityRR sourceAccount;
	public final TRANSACTION_TYPE type;
	

	public TransactionResponse(TRANSACTION_TYPE type, Transaction transaction) {
		this.type = type;
		this.id = transaction.id;
		this.amount = transaction.amount.getAmount();
		this.currency = transaction.amount.getCurrencyUnit().toString();
		this.createdAt = transaction.created;
		
		this.beneficiaryAccount = new TransactionalEntityRR(
				transaction.beneficiaryAccount.getId(), 
				transaction.beneficiaryAccount.getName());
		
		this.sourceAccount = new TransactionalEntityRR(
				transaction.sourceAccount.getId(), 
				transaction.sourceAccount.getName());
		
	}

}
