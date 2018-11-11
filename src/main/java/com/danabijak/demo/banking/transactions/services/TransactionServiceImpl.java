package com.danabijak.demo.banking.transactions.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.danabijak.demo.banking.entity.BankAccount;
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
public class TransactionServiceImpl implements TransactionService{
	
	@Autowired
	private TransactionRepository transactionRepo;

	@Override
	public Transaction porcess(TransactionIntent intent) throws TransactionServiceException {
		try {
			System.out.println("TransactionServiceImpl | process() | processing...");
			if(intent.isValid()) {
				System.out.println("TransactionServiceImpl | process() | intent is Valid");
				
				intent.source.getBankAccount().getBalance().decreaseTotal(intent.amount);
				
				System.out.println("TransactionServiceImpl | process() | 1");
				intent.beneficiary.getBankAccount().getBalance().increaseTotal(intent.amount);
				
				System.out.println("TransactionServiceImpl | process() | 2");
				Transaction transaction = new Transaction(
						intent.amount, 
						intent.beneficiary.getBankAccount(), 
						intent.source.getBankAccount(), 
						"Successfully made transaction");
				
				System.out.println("TransactionServiceImpl | process() | transactionRepo: " + transactionRepo);
				transactionRepo.save(transaction);
				
				System.out.println("TransactionServiceImpl | process() | transaction saved: " + transaction.toString());
				return transaction;
			}else {
				throw new TransactionServiceException("Transaction intent is not valid. Transaction not made!");
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
			throw new TransactionServiceException("Cannot process intent, error: " + e.getMessage());
		}
		
	}
	
	@Override
	public List<Transaction> getDebitTransactionsOf(BankAccount account) throws TransactionNotFoundException {
		// TODO: only return transactions that the id parameter is the source!
		List<Transaction> allTransactions = transactionRepo.findAll();
		List<Transaction> accountTransactions = new ArrayList<>();
		
		//TODO: Change this loop for smarter Repository find method!
		for(Transaction trans:allTransactions) {
			if(trans.sourceAccount.getId() == account.getId()) {
				accountTransactions.add(trans);
			}
		}
		
		if(accountTransactions.isEmpty())
			throw new TransactionNotFoundException(String.format("No debit transaction found")); 
		else 
			return accountTransactions;
	}
	
	@Override
	public List<Transaction> getCreditTransactionsOf(BankAccount account) throws TransactionNotFoundException {
		// TODO: only return transactions that the id parameter is the beneficiary
		List<Transaction> allTransactions = transactionRepo.findAll();
		List<Transaction> accountTransactions = new ArrayList<>();
		
		//TODO: Change this loop for smarter Repository find method!
		for(Transaction trans:allTransactions) {
			if(trans.beneficiaryAccount.getId() == account.getId()) {
				accountTransactions.add(trans);
			}
		}
		
		if(accountTransactions.isEmpty())
			throw new TransactionNotFoundException(String.format("No credit transaction found")); 
		else 
			return accountTransactions;
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
