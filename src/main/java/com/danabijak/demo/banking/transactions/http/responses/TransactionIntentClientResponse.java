package com.danabijak.demo.banking.transactions.http.responses;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

import com.danabijak.demo.banking.entity.TransactionIntentStatus;
import com.danabijak.demo.banking.entity.TransactionIntentStatus.TRANSFER_STATUS;
import com.danabijak.demo.banking.users.http.responses.TransactionalEntityClientR;

public class TransactionIntentClientResponse {
	public final long id;
	public final BigDecimal amount;
	public final Currency currency;
	public final Date createdAt;
	public final TransactionIntentStatus.TRANSFER_STATUS status;
	public final TransactionalEntityClientR beneficiary;
	public final TransactionalEntityClientR source;
	public final String details;
	public final boolean isValid;
	public TransactionIntentClientResponse(long id, BigDecimal amount, Currency currency, Date createdAt,
			TRANSFER_STATUS status, TransactionalEntityClientR beneficiary, TransactionalEntityClientR source,
			String details, boolean isValid) {
		super();
		this.id = id;
		this.amount = amount;
		this.currency = currency;
		this.createdAt = createdAt;
		this.status = status;
		this.beneficiary = beneficiary;
		this.source = source;
		this.details = details;
		this.isValid = isValid;
	}
	
	

}
