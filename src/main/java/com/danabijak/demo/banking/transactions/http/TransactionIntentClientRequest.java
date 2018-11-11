package com.danabijak.demo.banking.transactions.http;

import com.danabijak.demo.banking.model.MoneyClientRequest;
import com.danabijak.demo.banking.users.http.TransactionalEntityClientR;

public class TransactionIntentClientRequest {
	public TransactionalEntityClientR beneficiary;
	public TransactionalEntityClientR source;
	public MoneyClientRequest money;
	
	public TransactionIntentClientRequest() {}

}