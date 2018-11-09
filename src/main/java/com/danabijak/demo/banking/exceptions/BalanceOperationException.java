package com.danabijak.demo.banking.exceptions;

public class BalanceOperationException extends RuntimeException {
	public BalanceOperationException(String message) {
		super(message);
	}

}
