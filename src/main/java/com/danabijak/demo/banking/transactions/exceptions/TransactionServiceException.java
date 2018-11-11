package com.danabijak.demo.banking.transactions.exceptions;

public class TransactionServiceException extends RuntimeException {
	public TransactionServiceException(String message) {
		super(message);
	}

}
