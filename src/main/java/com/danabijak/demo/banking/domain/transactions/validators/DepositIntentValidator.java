package com.danabijak.demo.banking.domain.transactions.validators;

import com.danabijak.demo.banking.domain.accounts.entity.BankAccount;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntent;

public class DepositIntentValidator extends TransactionIntentValidatorImpl {

	@Override
	protected boolean entityBalanceAllowsFor(TransactionIntent intent) {
		if(beneficiaryBalanceLimitIsExceededAfterAmountOn(intent))
			return false;
		else
			return true;
	}
	
	@Override
	protected boolean entityTransactionLimitsAllowFor(TransactionIntent intent) {
		if(beneficiaryAllowedDepositLimitIsExceededByAmountOn(intent))
			return false;
		else
			return true;
	}
	
	
	
	private boolean beneficiaryBalanceLimitIsExceededAfterAmountOn(TransactionIntent intent) {
		return (BankAccount.DEFAULT_LIMITS.MAX_TOTAL_BALANCE.compareTo(intent.beneficiary.getBankAccount().getBalance().plus(intent.amount).getAmount()) < 0);
	}
	
	private boolean beneficiaryAllowedDepositLimitIsExceededByAmountOn(TransactionIntent intent) {
		return (intent.beneficiary.getLimits().getAllowedDeposit().compareTo(intent.amount.getAmount()) < 0);
	}

}
