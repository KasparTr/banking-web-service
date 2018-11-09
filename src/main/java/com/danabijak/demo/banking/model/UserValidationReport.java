package com.danabijak.demo.banking.model;

import java.util.List;
import java.util.Optional;

import com.danabijak.demo.banking.exceptions.ReportException;

// DTO
public class UserValidationReport {
	public final boolean valid;
	public final Optional<List<String>> faultDescriptions;
	
	public UserValidationReport(boolean valid, Optional<List<String>> faultDescriptions) {
		super();
		this.valid = valid;
		this.faultDescriptions = faultDescriptions;
	}
	
	public UserValidationReport(boolean valid) throws ReportException {
		super();
		if(!valid) throw new ReportException("Validation reports reporting fault, require details");
		this.valid = valid;
		this.faultDescriptions = Optional.empty();
	}
	
	public String generateStringMessage() {
		String message;
		if(valid) {
			message = "User validated";
		}else {
			StringBuilder sb = new StringBuilder();
			for (String s : faultDescriptions.get())
			{
			    sb.append(s);
			    sb.append("\t");
			}

			message = sb.toString();
		}
		return message;
	}
}
