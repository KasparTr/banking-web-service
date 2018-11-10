package com.danabijak.demo.banking.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.danabijak.demo.banking.entity.Transaction;
import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.entity.TransactionalEntity;
import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.exceptions.TransactionNotFoundException;
import com.danabijak.demo.banking.exceptions.TransactionServiceException;
import com.danabijak.demo.banking.exceptions.UserNotFoundException;
import com.danabijak.demo.banking.repositories.TransactionIntentRepository;
import com.danabijak.demo.banking.repositories.TransactionRepository;

@Component
public abstract class TransactionServiceImpl implements TransactionService{
	
	@Autowired
	private TransactionRepository transactionRepo;

	@Override
	public Transaction porcess(TransactionIntent intent) throws TransactionServiceException {
		System.out.println("TransactionServiceImpl | process() | processing...");
		if(intent.isValid()) {
			System.out.println("TransactionServiceImpl | process() | intent is valid");
			
			intent.source.getBankAccount().getBalance().decreaseTotal(intent.amount);
			intent.beneficiary.getBankAccount().getBalance().increaseTotal(intent.amount);
			Transaction transaction = new Transaction(intent.amount, intent.beneficiary, intent.source, "Successfully made transaction");
			transactionRepo.save(transaction);
			
			System.out.println("TransactionServiceImpl | process() | transaction saved: " + transaction.toString());
			return transaction;
		}else {
			throw new TransactionServiceException("Transaction intent is not valid. Transaction not made!");
		}
	}
	
	@Override
	public List<Transaction> getAllTransactionsOf(long id) throws TransactionNotFoundException {
		// TODO: only return transactions that the id parameter is either beneficiary, or source!
		List<Transaction> transactions = transactionRepo.findAll();
		
		if(transactions.isEmpty())
			throw new TransactionNotFoundException(String.format("No transaction found")); 
		else 
			return transactions;
	}

	@Override
	public Transaction findTransactionBy(long id) throws TransactionNotFoundException {
		Optional<Transaction> transaction = transactionRepo.findById(id);
		
		if(transaction.isPresent())
			return transaction.get();
		else 
			throw new TransactionNotFoundException(String.format("Transaction with ID %s not found", id));

	}
	
	

}
