package com.danabijak.demo.banking.services;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.model.ValidationReport;
import com.danabijak.demo.banking.validators.TransactionIntentValidator;
import com.danabijak.demo.banking.validators.WithdrawIntentValidator;

@Component
@Resource(name="withdrawIntentService")
public class WithdrawIntentService extends TransactionIntentServiceImpl{

	@Override
	protected ValidationReport validateIntent(TransactionIntent intent) {
		TransactionIntentValidator validator = new WithdrawIntentValidator();
		return validator.validate(intent);
	}

	/**
	 * NB! Publishing intents to messaging channel is not implement yet.
	 * TOOD: Instead of directly sending intent to TransactionSerice for processing, publish the intent to the intent pool and 
	 * 	have the TransactionService (as a subscriber) process the intents.
	 */
	@Override
	protected void publishIntent(TransactionIntent intent) {
		// TODO: HANDLE LOGIC HOW WITHDRAWAL INTENTS ARE PUBLISHED
		// transfer.source.currentMaxWithdraw -= transfer.amount
		// ...
		
		
		// 
	}

}
