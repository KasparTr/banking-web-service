package com.danabijak.demo.banking.domain.transactions.exceptions;

public class BalanceOperationException extends RuntimeException {
	public BalanceOperationException(String message) {
		super(message);
	}

}
