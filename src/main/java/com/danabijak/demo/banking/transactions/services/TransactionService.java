package com.danabijak.demo.banking.transactions.services;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.danabijak.demo.banking.entity.BankAccount;
import com.danabijak.demo.banking.entity.Transaction;
import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.entity.TransactionalEntity;
import com.danabijak.demo.banking.transactions.exceptions.TransactionServiceException;
import com.danabijak.demo.banking.transactions.model.AccountTransactions;

@Component
public interface TransactionService {
	
	@Async("asyncExecutor")
	public CompletableFuture<Transaction> porcess(TransactionIntent intent) throws TransactionServiceException;
	
	@Async("asyncExecutor")
	public void porcessAllIntents() throws TransactionServiceException;
	
	/**
	 * Get specific transaction using transaction id.
	 * @param id
	 * @return
	 * @throws TransactionServiceException
	 */
	@Async("asyncExecutor")
	public CompletableFuture<Transaction> findTransactionBy(long id) throws TransactionServiceException;


}
