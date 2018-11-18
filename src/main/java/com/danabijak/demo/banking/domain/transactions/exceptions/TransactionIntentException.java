package com.danabijak.demo.banking.domain.transactions.exceptions;

// TODO: Break this into 
// - intent publish exception
// - intent validation exception
// - other...
public class TransactionIntentException extends RuntimeException {
	public TransactionIntentException(String message) {
		super(message);
	}
}
