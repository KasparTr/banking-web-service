package com.danabijak.demo.banking.domain.transactions.services;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.danabijak.demo.banking.domain.transactions.entity.Transaction;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntent;
import com.danabijak.demo.banking.domain.transactions.exceptions.TransactionNotFoundException;
import com.danabijak.demo.banking.domain.transactions.exceptions.TransactionServiceException;

@Service
public interface TransactionService {
	
	/**
	 * Process the intent by looking if intent is valid and then making the balance changes according to the instructions on the intent.
	 * @param intent
	 * @return
	 * @throws TransactionServiceException
	 */
	@Async("asyncExecutor")
	public CompletableFuture<Transaction> process(TransactionIntent intent) throws TransactionServiceException;
	
	/**
	 * Process all intents available in the channel by making balance changes according to the instructions on the intent.
	 * @throws TransactionServiceException
	 */
	@Async("asyncExecutor")
	public void porcessAllIntents() throws TransactionServiceException;
	
	/**
	 * Get specific transaction using transaction id.
	 * @param id
	 * @return
	 * @throws TransactionServiceException
	 */
	@Async("asyncExecutor")
	public CompletableFuture<Transaction> findTransactionBy(long id) throws TransactionNotFoundException;


}
