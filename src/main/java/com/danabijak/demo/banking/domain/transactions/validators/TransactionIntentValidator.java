package com.danabijak.demo.banking.domain.transactions.validators;

import com.danabijak.demo.banking.core.ValidationReport;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntent;

public interface TransactionIntentValidator {
	
	public ValidationReport validate(TransactionIntent intent);
}
