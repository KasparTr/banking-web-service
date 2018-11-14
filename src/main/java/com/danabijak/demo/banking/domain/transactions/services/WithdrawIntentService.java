package com.danabijak.demo.banking.domain.transactions.services;


import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.danabijak.demo.banking.core.ValidationReport;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntent;
import com.danabijak.demo.banking.domain.transactions.validators.TransactionIntentValidator;
import com.danabijak.demo.banking.domain.transactions.validators.WithdrawIntentValidator;

@Service
@Resource(name="withdrawIntentService")
public class WithdrawIntentService extends TransactionIntentServiceImpl{
	

	@Override
	protected ValidationReport validateIntent(TransactionIntent intent) {
		TransactionIntentValidator validator = new WithdrawIntentValidator();
		return validator.validate(intent);
	}
	
	@Override
	protected void reserverParticipantsBalance(TransactionIntent intent) {
		intent.source.getLimits().decreaseAllowedWithdrawal(intent.amount.getAmount());
	}

}
