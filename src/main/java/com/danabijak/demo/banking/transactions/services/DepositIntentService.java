package com.danabijak.demo.banking.transactions.services;

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
	
	@Override
	protected void reserverParticipantsBalance(TransactionIntent intent) {
		intent.beneficiary.getLimits().decreaseAllowedDeposit(intent.amount.getAmount());
	}
}
