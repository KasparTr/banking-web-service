package com.danabijak.demo.banking.domain.users.factories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.danabijak.demo.banking.domain.accounts.entity.BankAccount;
import com.danabijak.demo.banking.domain.accounts.valueobjects.AccountStatementResponse;
import com.danabijak.demo.banking.domain.accounts.valueobjects.AccountTransactions;
import com.danabijak.demo.banking.domain.transactions.entity.Transaction;
import com.danabijak.demo.banking.domain.transactions.valueobjects.TransactionResponse;

@Component
public class BankAccountStatementFactory {
	
	public AccountStatementResponse generateStatement(
			BankAccount account,
			AccountTransactions transactions) {
		
		List<TransactionResponse> transClientResponses = new ArrayList<>();		
		
		for(Transaction trans:transactions.creditTransactions) {
			transClientResponses.add(new TransactionResponse(
					TransactionResponse.TRANSACTION_TYPE.CREDIT,
					trans));
		}
		
		for(Transaction trans:transactions.debitTransactions) {
			transClientResponses.add(new TransactionResponse(
					TransactionResponse.TRANSACTION_TYPE.DEBIT,
					trans));
		}
		
		return new AccountStatementResponse(
				account,
				transClientResponses,
				account.getBalance().getAmount());

	}

}
