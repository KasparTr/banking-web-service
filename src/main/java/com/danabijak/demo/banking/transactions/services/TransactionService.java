package com.danabijak.demo.banking.transactions.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.danabijak.demo.banking.entity.BankAccount;
import com.danabijak.demo.banking.entity.Transaction;
import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.entity.TransactionalEntity;
import com.danabijak.demo.banking.transactions.exceptions.TransactionServiceException;
import com.danabijak.demo.banking.transactions.model.AccountTransactions;

@Component
public interface TransactionService {
	public Transaction porcess(TransactionIntent intent) throws TransactionServiceException;
	public void porcessAllIntents() throws TransactionServiceException;
	
	
	public List<Transaction> getDebitTransactionsOf(BankAccount account) throws TransactionServiceException;
	public List<Transaction> getCreditTransactionsOf(BankAccount account) throws TransactionServiceException;
	
	public AccountTransactions getTransactionsOf(BankAccount account) throws TransactionServiceException;
	
	public Transaction findTransactionBy(long id) throws TransactionServiceException;

}
