package com.danabijak.demo.banking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.exceptions.TransactionIntentPublishException;
import com.danabijak.demo.banking.model.TransactionIntentPublishAttemptReport;
import com.danabijak.demo.banking.model.ValidationReport;
import com.danabijak.demo.banking.repositories.TransactionIntentRepository;
import com.danabijak.demo.banking.repositories.UserRepository;

@Component
public abstract class TransactionIntentServiceImpl implements TransactionIntentService{
	
	@Autowired
	private TransactionIntentRepository transactionIntentRepo;
	
	//TODO: ASYNCH CHECK!!!!
	public TransactionIntentPublishAttemptReport attemptToPublishIntent(TransactionIntent intent){	// MUST BE ASYNC!!
		try {
			TransactionIntentPublishAttemptReport report;
			ValidationReport validationReport = validateIntent(intent);
			
			if(validationReport.valid) {
				transactionIntentRepo.save(intent);
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
