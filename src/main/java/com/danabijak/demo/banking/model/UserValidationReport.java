package com.danabijak.demo.banking.model;

import java.util.List;
import java.util.Optional;

// DTO
public class UserValidationReport {
	public final boolean valid;
	public Optional<List<String>> faultyParts;
	
	public UserValidationReport(boolean valid, Optional<List<String>> faultyParts) {
		super();
		this.valid = valid;
		this.faultyParts = faultyParts;
	}
	
	public UserValidationReport(boolean valid) {
		super();
		this.valid = valid;
	}
	
	public String generateStringMessage() {
		String message;
		if(valid) {
			message = "User validated";
		}else {
			StringBuilder sb = new StringBuilder();
			for (String s : faultyParts.get())
			{
			    sb.append(s);
			    sb.append("\t");
			}

			message = sb.toString();
		}
		return message;
	}
}
