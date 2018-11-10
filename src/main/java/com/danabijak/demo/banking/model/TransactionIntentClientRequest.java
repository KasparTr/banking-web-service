package com.danabijak.demo.banking.model;

public class TransactionIntentClientRequest {
	public TransactionalEntityClientRequest beneficiary;
	public TransactionalEntityClientRequest source;
	public MoneyClientRequest money;
	
	public TransactionIntentClientRequest() {}

}