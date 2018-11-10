package com.danabijak.demo.banking.model;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.entity.TransactionIntentStatus;
import com.danabijak.demo.banking.users.http.responses.TransactionalEntityClientR;

public class TransactionIntentPublishAttemptReport {
	public final long id;
	public final BigDecimal amount;
	public final String currency;
	public final Date createdAt;
	public final TransactionIntentStatus.TRANSFER_STATUS status;
	public final TransactionalEntityClientR beneficiary;
	public final TransactionalEntityClientR source;
	public final String details;
	public final boolean isValid;
	

	public TransactionIntentPublishAttemptReport(boolean b, String details, TransactionIntent intent) {
		this.id = intent.id;
		this.amount = intent.amount.getAmount();
		this.currency = intent.amount.getCurrencyUnit().toString();
		this.createdAt = intent.createdAt;
		this.status = intent.status.status;
		
		this.beneficiary = new TransactionalEntityClientR(intent.beneficiary.getId(), intent.beneficiary.getName());
		this.source = new TransactionalEntityClientR(intent.source.getId(), intent.source.getName());
		this.details = details;
		this.isValid = intent.isValid();
	}

}
