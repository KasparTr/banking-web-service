package com.danabijak.demo.banking.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserObjectNotValidException extends RuntimeException {
	public UserObjectNotValidException(String message) {
		super(message);
	}
}