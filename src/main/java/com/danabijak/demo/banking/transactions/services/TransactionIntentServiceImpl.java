package com.danabijak.demo.banking.transactions.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.exceptions.TransactionIntentPublishException;
import com.danabijak.demo.banking.model.ValidationReport;
import com.danabijak.demo.banking.repositories.TransactionIntentRepository;
import com.danabijak.demo.banking.repositories.UserRepository;
import com.danabijak.demo.banking.transactions.http.TransactionIntentClientResponse;

@Component
public abstract class TransactionIntentServiceImpl implements TransactionIntentService{
	
	@Autowired
	private TransactionIntentRepository transactionIntentRepo;
	
	//TODO: ASYNCH CHECK!!!!
	public TransactionIntent attemptPublish(TransactionIntent intent){	// MUST BE ASYNC!!
		ValidationReport validationReport = validateIntent(intent);
		
		if(validationReport.valid) {
			transactionIntentRepo.save(intent);
			reserverParticipantsBalance(intent);
			return publish(intent);
		}else {
			throw new TransactionIntentPublishException("Intent is Not Valid");
		}
	}
	

	/**
	 * NB! Publishing intents to messaging channel is not implement yet.
	 * TOOD: Instead of directly sending intent to TransactionSerice for processing, publish the intent to the intent pool and 
	 * 	have the TransactionService (as a subscriber) process the intents.
	 */
	protected TransactionIntent publish(TransactionIntent intent) {
		// TODO: Implement publishing to PUB/SUB channel. 
		// NB! if that fails keep in mind to restore booked amount!

		return intent;
	}

	protected abstract ValidationReport validateIntent(TransactionIntent intent);
	protected abstract void reserverParticipantsBalance(TransactionIntent intent);


}
