package com.danabijak.demo.banking.domain.transactions.services;

import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.danabijak.demo.banking.core.ValidationReport;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntent;
import com.danabijak.demo.banking.domain.transactions.exceptions.TransactionIntentPublishException;
import com.danabijak.demo.banking.domain.transactions.repositories.TransactionIntentRepository;

@Service
public abstract class TransactionIntentServiceImpl implements TransactionIntentService{

	@Autowired
	private TransactionIntentRepository transactionIntentRepo;
	
	@Override
	@Async("asyncExecutor")
	public CompletableFuture<TransactionIntent> publish(TransactionIntent intent) throws TransactionIntentPublishException {
	    ValidationReport validationReport = validateIntent(intent);
		
		if(validationReport.valid) {
			transactionIntentRepo.save(intent);
			reserverParticipantsBalance(intent);
			publishToChannel(intent);
			return CompletableFuture.completedFuture(intent);
		}else {
			throw new TransactionIntentPublishException("Transaction intent not publised. Errors: " + validationReport.generateStringMessage());
		}
	}

	/**
	 * NB! Publishing intents to messaging channel is not implement yet.
	 * TODO: Instead of directly sending intent to TransactionSerice for processing, publish the intent to the intent pool and 
	 * 	have the TransactionService (as a subscriber) process the intents.
	 */
	protected void publishToChannel(TransactionIntent intent) {
		// TODO: Implement publishing to PUB/SUB channel. 
		// NB! if that fails keep in mind to undo reserver balance/limits!
		
	}

	protected abstract ValidationReport validateIntent(TransactionIntent intent);
	protected abstract void reserverParticipantsBalance(TransactionIntent intent);

}
