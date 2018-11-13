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
import com.danabijak.demo.banking.transactions.validators.TransactionIntentValidator;
import com.danabijak.demo.banking.transactions.validators.WithdrawIntentValidator;

@Service
@Resource(name="withdrawIntentService")
public class WithdrawIntentService extends TransactionIntentServiceImpl{
//public class WithdrawIntentService implements TransactionIntentService{
	

	@Override
	protected ValidationReport validateIntent(TransactionIntent intent) {
		TransactionIntentValidator validator = new WithdrawIntentValidator();
		return validator.validate(intent);
	}
	
	@Override
	protected void reserverParticipantsBalance(TransactionIntent intent) {
		intent.source.getLimits().decreaseAllowedWithdrawal(intent.amount.getAmount());
	}

	@Override
	protected TransactionIntent makeTransactionIntent(TransactionalEntity user, TransactionalEntity bank, Money money) {
		return new TransactionIntentBuilder()
				.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Withdraw"))
				.source(user)
				.beneficiary(bank)
				.amount(money)
				.build();
	}
}
