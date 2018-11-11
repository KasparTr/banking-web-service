package com.danabijak.demo.banking.users.factories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.danabijak.demo.banking.entity.Transaction;
import com.danabijak.demo.banking.entity.TransactionalEntity;
import com.danabijak.demo.banking.transactions.http.BankAccountStatementClientResponse;
import com.danabijak.demo.banking.transactions.http.TransactionClientResponse;

@Component
public class BankAccountStatementFactory {
	
	public BankAccountStatementClientResponse generateStatement(
			TransactionalEntity entity, 
			long accountId,
			List<Transaction> cTransactions, 
			List<Transaction> dTransactions) {
		
		List<TransactionClientResponse> transClientResponses = new ArrayList<>();		
		
		for(Transaction trans:cTransactions) {
			transClientResponses.add(new TransactionClientResponse(
					TransactionClientResponse.TRANSACTION_TYPE.CREDIT,
					trans));
		}
		
		for(Transaction trans:dTransactions) {
			transClientResponses.add(new TransactionClientResponse(
					TransactionClientResponse.TRANSACTION_TYPE.DEBIT,
					trans));
		}
		
		return new BankAccountStatementClientResponse(accountId, entity.getName(), transClientResponses);

	}

}
