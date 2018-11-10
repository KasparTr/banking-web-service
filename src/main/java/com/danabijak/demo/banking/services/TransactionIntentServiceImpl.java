package com.danabijak.demo.banking.services;

import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.exceptions.TransactionIntentPublishException;
import com.danabijak.demo.banking.model.TransactionIntentPublishAttemptReport;
import com.danabijak.demo.banking.model.ValidationReport;

public abstract class TransactionIntentServiceImpl implements TransactionIntentService{
	
	//TODO: ASYNCH CHECK!!!!
	public TransactionIntentPublishAttemptReport attemptToPublishIntent(TransactionIntent intent){	// MUST BE ASYNC!!
		try {
			TransactionIntentPublishAttemptReport report;
			ValidationReport validationReport = validateIntent(intent);
			
			if(validationReport.valid) {
				publishIntent(intent);
				report = new TransactionIntentPublishAttemptReport(true, "success", intent);
			}	
			else
				report = new TransactionIntentPublishAttemptReport(false, validationReport.generateStringMessage(), intent);

			return report;
		}catch(Exception e) {
			throw new TransactionIntentPublishException(e.getMessage());
		}
	}

	protected abstract ValidationReport validateIntent(TransactionIntent intent);
	protected abstract void publishIntent(TransactionIntent intent);

}
