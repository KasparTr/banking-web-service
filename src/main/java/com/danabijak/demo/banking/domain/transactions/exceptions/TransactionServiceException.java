package com.danabijak.demo.banking.domain.transactions.exceptions;

public class TransactionServiceException extends RuntimeException {
	public TransactionServiceException(String message) {
		super(message);
	}

}
