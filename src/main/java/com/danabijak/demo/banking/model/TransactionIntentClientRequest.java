package com.danabijak.demo.banking.model;

import com.danabijak.demo.banking.users.http.responses.TransactionalEntityClientR;

public class TransactionIntentClientRequest {
	public TransactionalEntityClientR beneficiary;
	public TransactionalEntityClientR source;
	public MoneyClientRequest money;
	
	public TransactionIntentClientRequest() {}

}