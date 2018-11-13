package com.danabijak.demo.banking.transactions.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.danabijak.demo.banking.accounts.entity.BankAccount;
import com.danabijak.demo.banking.transactions.entity.TransactionIntent;
import com.danabijak.demo.banking.transactions.model.ValidationReport;

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
