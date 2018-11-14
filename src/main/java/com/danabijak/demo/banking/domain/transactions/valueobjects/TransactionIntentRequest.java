package com.danabijak.demo.banking.domain.transactions.valueobjects;

import com.danabijak.demo.banking.domain.users.valueobjects.TransactionalEntityRR;

public class TransactionIntentRequest {
	public TransactionalEntityRR beneficiary;
	public TransactionalEntityRR source;
	public MoneyRequest money;
	
	public TransactionIntentRequest() {}

}