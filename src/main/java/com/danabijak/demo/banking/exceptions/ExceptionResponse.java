package com.danabijak.demo.banking.exceptions;

import java.util.Date;

// This is a DTO (immutable data transfer object)
public class ExceptionResponse {
	public final Date timestamp;
	public final String messager;
	public final String details;
	
	public ExceptionResponse(Date timestamp, String messager, String details) {
		super();
		this.timestamp = timestamp;
		this.messager = messager;
		this.details = details;
	}
}
