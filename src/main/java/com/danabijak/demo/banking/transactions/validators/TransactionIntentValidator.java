package com.danabijak.demo.banking.transactions.validators;

import com.danabijak.demo.banking.transactions.entity.TransactionIntent;
import com.danabijak.demo.banking.transactions.model.ValidationReport;

public interface TransactionIntentValidator {
	
	public ValidationReport validate(TransactionIntent intent);
}
