package com.danabijak.demo.banking.core;

import java.util.List;
import java.util.Optional;

import com.danabijak.demo.banking.domain.transactions.exceptions.ReportException;

// DTO
public class ValidationReport {
	public final boolean valid;
	public final Optional<List<String>> faultDescriptions;
	
	public ValidationReport(boolean valid, Optional<List<String>> faultDescriptions) {
		super();
		this.valid = valid;
		this.faultDescriptions = faultDescriptions;
	}
	
	public ValidationReport(boolean valid) throws ReportException {
		super();
		if(!valid) throw new ReportException("Validation reports that are reporting fault, require descriptions");
		this.valid = valid;
		this.faultDescriptions = Optional.empty();
	}
	
	public String generateStringMessage() {
		String message;
		if(valid) {
			message = "Is Valid";
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
