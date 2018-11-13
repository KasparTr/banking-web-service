package com.danabijak.demo.banking.transactions.services;


import javax.annotation.Resource;
import org.joda.money.Money;
import org.springframework.stereotype.Service;

import com.danabijak.demo.banking.transactions.entity.TransactionIntent;
import com.danabijak.demo.banking.transactions.entity.TransactionIntentStatus;
import com.danabijak.demo.banking.transactions.entity.TransactionalEntity;
import com.danabijak.demo.banking.transactions.entity.TransactionIntentStatus.TRANSFER_STATUS;
import com.danabijak.demo.banking.transactions.model.TransactionIntentBuilder;
import com.danabijak.demo.banking.transactions.model.ValidationReport;
import com.danabijak.demo.banking.transactions.validators.DepositIntentValidator;
import com.danabijak.demo.banking.transactions.validators.TransactionIntentValidator;

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
