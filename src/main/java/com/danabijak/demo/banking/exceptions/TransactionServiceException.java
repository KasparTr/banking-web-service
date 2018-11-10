package com.danabijak.demo.banking.exceptions;

public class TransactionServiceException extends RuntimeException {
	public TransactionServiceException(String message) {
		super(message);
	}

}
