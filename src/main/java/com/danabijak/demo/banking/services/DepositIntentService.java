package com.danabijak.demo.banking.services;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.model.ValidationReport;
import com.danabijak.demo.banking.validators.DepositIntentValidator;
import com.danabijak.demo.banking.validators.TransactionIntentValidator;

@Component
@Resource(name="depositIntentService")
public class DepositIntentService extends TransactionIntentServiceImpl{

	@Override
	protected ValidationReport validateIntent(TransactionIntent intent) {
		TransactionIntentValidator validator = new DepositIntentValidator();
		return validator.validate(intent);
	}

	/**
	 * NB! Publishing intents to messaging channel is not implement yet.
	 * TOOD: Instead of directly sending intent to TransactionSerice for processing, publish the intent to the intent pool and 
	 * 	have the TransactionService (as a subscriber) process the intents.
	 */
	@Override
	protected void publishIntent(TransactionIntent intent) {
		if(intent.isValid()) {
			// lower the deposit allowLimits of the beneficiary
			intent.beneficiary.getLimits().decreaseAllowedDeposit(intent.amount.getAmount());
			
			// TODO: Replace handover to TransactionService with a publishing to a TransactionIntentPool
			TransactionService depositService = new TransactionServiceImpl();
			depositService.porcess(intent);
		}
	}

}
