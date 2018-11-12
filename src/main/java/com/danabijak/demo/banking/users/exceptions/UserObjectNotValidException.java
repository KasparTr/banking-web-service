package com.danabijak.demo.banking.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserObjectNotValidException extends RuntimeException {
	public UserObjectNotValidException(String message) {
		super(message);
	}
}