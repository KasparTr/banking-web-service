package com.danabijak.demo.banking.domain.transactions.services;


import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.danabijak.demo.banking.core.ValidationReport;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntent;
import com.danabijak.demo.banking.domain.transactions.validators.DepositIntentValidator;
import com.danabijak.demo.banking.domain.transactions.validators.TransactionIntentValidator;

@Service
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
