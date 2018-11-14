package com.danabijak.demo.banking.domain.transactions.valueobjects;

import java.math.BigDecimal;
import java.util.Date;

import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntent;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntentStatus;
import com.danabijak.demo.banking.domain.users.valueobjects.TransactionalEntityRR;

public class TransactionIntentResponse {
	public final long id;
	public final BigDecimal amount;
	public final String currency;
	public final Date createdAt;
	public final TransactionIntentStatus.TRANSFER_STATUS status;
	public final TransactionalEntityRR beneficiary;
	public final TransactionalEntityRR source;
	public final String details;
	public final boolean isValid;
	

	public TransactionIntentResponse(boolean b, String details, TransactionIntent intent) {
		this.id = intent.id;
		this.amount = intent.amount.getAmount();
		this.currency = intent.amount.getCurrencyUnit().toString();
		this.createdAt = intent.createdAt;
		this.status = intent.status.status;
		
		this.beneficiary = new TransactionalEntityRR(
				intent.beneficiary.getId(), 
				intent.beneficiary.getName());
		
		this.source = new TransactionalEntityRR(
				intent.source.getId(), 
				intent.source.getName());
		this.details = details;
		this.isValid = intent.isValid();
	}

}
