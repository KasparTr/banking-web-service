package com.danabijak.demo.banking.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UserSavingException extends RuntimeException {

	public UserSavingException(String message) {
		super(message);
	}
}
