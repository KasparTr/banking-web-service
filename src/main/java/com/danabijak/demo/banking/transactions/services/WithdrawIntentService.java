package com.danabijak.demo.banking.transactions.services;

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
	
	@Override
	protected void reserverParticipantsBalance(TransactionIntent intent) {
		// transfer.source.currentMaxWithdraw -= transfer.amount
	}

}
