package com.danabijak.demo.banking.transactions.exceptions;

// TODO: Break this into 
// - intent publish exception
// - intent validation exception
// - other...
public class TransactionIntentPublishException extends RuntimeException {
	public TransactionIntentPublishException(String message) {
		super(message);
	}
}
