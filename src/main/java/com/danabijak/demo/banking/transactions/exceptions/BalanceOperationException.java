package com.danabijak.demo.banking.transactions.exceptions;

public class BalanceOperationException extends RuntimeException {
	public BalanceOperationException(String message) {
		super(message);
	}

}
