package com.danabijak.demo.banking.users.factories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.danabijak.demo.banking.entity.BankAccount;
import com.danabijak.demo.banking.entity.Transaction;
import com.danabijak.demo.banking.entity.TransactionalEntity;
import com.danabijak.demo.banking.transactions.http.BankAccountStatementClientResponse;
import com.danabijak.demo.banking.transactions.http.TransactionClientResponse;
import com.danabijak.demo.banking.transactions.model.AccountTransactions;

@Component
public class BankAccountStatementFactory {
	
	public BankAccountStatementClientResponse generateStatement(
			TransactionalEntity entity, 
			BankAccount account,
			AccountTransactions transactions) {
		
		List<TransactionClientResponse> transClientResponses = new ArrayList<>();		
		
		for(Transaction trans:transactions.creditTransactions) {
			transClientResponses.add(new TransactionClientResponse(
					TransactionClientResponse.TRANSACTION_TYPE.CREDIT,
					trans));
		}
		
		for(Transaction trans:transactions.debitTransactions) {
			transClientResponses.add(new TransactionClientResponse(
					TransactionClientResponse.TRANSACTION_TYPE.DEBIT,
					trans));
		}
		
		return new BankAccountStatementClientResponse(
				account.getId(), 
				entity.getName(), 
				transClientResponses,
				account.getBalance().getAmount());

	}

}
