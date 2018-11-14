package com.danabijak.demo.banking.domain.transactions.validators;

import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntent;

public class WithdrawIntentValidator extends TransactionIntentValidatorImpl {


	@Override
	protected boolean entityBalanceAllowsFor(TransactionIntent intent) {
		if(sourceBalanceIsLessThanAmountOn(intent))
			return false;
		else
			return true;
	}
	
	@Override
	protected boolean entityTransactionLimitsAllowFor(TransactionIntent intent) {
		if(sourceAllowedWithdrawLimitIsExceededByAmountOn(intent))
			return false;
		else
			return true;
	}

	
	protected boolean sourceBalanceIsLessThanAmountOn(TransactionIntent intent) {
		return intent.source.getBankAccount().getBalance().isLessThan(intent.amount);
	}

	protected boolean sourceAllowedWithdrawLimitIsExceededByAmountOn(TransactionIntent intent) {
		return (intent.source.getLimits().getAllowedWithdrawal().compareTo(intent.amount.getAmount()) < 0);

	}

}
