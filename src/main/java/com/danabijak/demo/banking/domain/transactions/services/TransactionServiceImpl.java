package com.danabijak.demo.banking.domain.transactions.services;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.danabijak.demo.banking.domain.transactions.entity.Transaction;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntent;
import com.danabijak.demo.banking.domain.transactions.exceptions.TransactionNotFoundException;
import com.danabijak.demo.banking.domain.transactions.exceptions.TransactionServiceException;
import com.danabijak.demo.banking.domain.transactions.repositories.TransactionIntentRepository;
import com.danabijak.demo.banking.domain.transactions.repositories.TransactionRepository;

@Service
public abstract class TransactionServiceImpl implements TransactionService{
	
	@Autowired
	private TransactionRepository transactionRepo;
	
	@Autowired
	private TransactionIntentRepository transactionIntentRepo;
	
	@Override
	@Async("asyncExecutor")
	public void porcessAllIntents() throws TransactionServiceException {
		List<TransactionIntent> allIntents = transactionIntentRepo.findAll();
		for(TransactionIntent i:allIntents) {
			if(!i.isPaid()) {
				processIntent(i);
			}
		}
	}
		
	@Override
	@Async("asyncExecutor")
	public CompletableFuture<Transaction> process(TransactionIntent intent) throws TransactionServiceException {
		// TODO: Validate intent here against bank account and entity limits. Don't just believe the intent flag
		try {
			if(intent.isValid()) {				
				return CompletableFuture.completedFuture(processIntent(intent));
			}else {
				throw new TransactionServiceException("Transaction intent is not valid. Transaction not made!");
			}
		}catch(Exception e) {
			throw new TransactionServiceException("Cannot process intent, error: " + e.getMessage());
		}
		
	}
	
	@Override
	@Async("asyncExecutor")
	public CompletableFuture<Transaction> findTransactionBy(long id) throws TransactionNotFoundException {
		Optional<Transaction> transaction = transactionRepo.findById(id);
		
		if(transaction.isPresent())
			return CompletableFuture.completedFuture(transaction.get());
		else 
			throw new TransactionNotFoundException(String.format("Transaction with ID %s not found", id));

	}
	
	private Transaction processIntent(TransactionIntent intent) throws TransactionServiceException{
		updateBalances(intent);		
		
		Transaction transaction = new Transaction(
				intent.amount, 
				intent.beneficiary.getBankAccount(), 
				intent.source.getBankAccount(), 
				"Successfully made transaction");
		
		transactionRepo.save(transaction);
		intent.setPaidTo(true);
		return transaction;
	}
	
	protected abstract void updateBalances(TransactionIntent intent);



}
