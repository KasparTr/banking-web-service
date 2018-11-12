package com.danabijak.demo.banking.transactions.services;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.infra.repositories.TransactionIntentRepository;
import com.danabijak.demo.banking.transactions.exceptions.TransactionIntentPublishException;
import com.danabijak.demo.banking.transactions.model.TransactionClientRequest;
import com.danabijak.demo.banking.transactions.model.ValidationReport;
import com.danabijak.demo.banking.validators.TransactionIntentValidator;
import com.danabijak.demo.banking.validators.WithdrawIntentValidator;

@Service
//@Resource(name="withdrawIntentService")
//public class WithdrawIntentService extends TransactionIntentServiceImpl{
public class WithdrawIntentService implements TransactionIntentService{
	
	@Autowired
	private TransactionIntentRepository transactionIntentRepo;
	
	@Override
	@Async("asyncExecutor")
	public CompletableFuture<TransactionIntent> publish(TransactionClientRequest intentRequest) throws TransactionIntentPublishException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public TransactionIntent attemptPublish(TransactionIntent intent){
		ValidationReport validationReport = validateIntent(intent);
		
		if(validationReport.valid) {
			System.out.println("TransactionIntentServiceImpl | attemptPublish() | intent validated: " + intent.toString());
			transactionIntentRepo.save(intent);
			System.out.println("TransactionIntentServiceImpl | attemptPublish() | intent saved");

			reserverParticipantsBalance(intent);
			return publish(intent);
		}else {
			throw new TransactionIntentPublishException("Transaction intent not publised. Errors: " + validationReport.generateStringMessage());
		}
	}
	
	/**
	 * NB! Publishing intents to messaging channel is not implement yet.
	 * TODO: Instead of directly sending intent to TransactionSerice for processing, publish the intent to the intent pool and 
	 * 	have the TransactionService (as a subscriber) process the intents.
	 */
	protected TransactionIntent publish(TransactionIntent intent) {
		// TODO: Implement publishing to PUB/SUB channel. 
		// NB! if that fails keep in mind to undo reserver balance/limits!

		return intent;
	}

	//@Override
	protected ValidationReport validateIntent(TransactionIntent intent) {
		TransactionIntentValidator validator = new WithdrawIntentValidator();
		return validator.validate(intent);
	}
	
	//@Override
	protected void reserverParticipantsBalance(TransactionIntent intent) {
		intent.source.getLimits().decreaseAllowedWithdrawal(intent.amount.getAmount());
	}

	

}
