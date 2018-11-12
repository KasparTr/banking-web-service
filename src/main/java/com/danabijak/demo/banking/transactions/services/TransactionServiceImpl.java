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
import com.danabijak.demo.banking.infra.repositories.TransactionIntentRepository;
import com.danabijak.demo.banking.infra.repositories.TransactionRepository;
import com.danabijak.demo.banking.transactions.exceptions.TransactionNotFoundException;
import com.danabijak.demo.banking.transactions.exceptions.TransactionServiceException;
import com.danabijak.demo.banking.transactions.model.AccountTransactions;
import com.danabijak.demo.banking.users.exceptions.UserNotFoundException;

@Component
public class TransactionServiceImpl implements TransactionService{
	
	@Autowired
	private TransactionRepository transactionRepo;

	@Override
	public Transaction porcess(TransactionIntent intent) throws TransactionServiceException {
		try {
			if(intent.isValid()) {				
				intent.source.getBankAccount().getBalance().decreaseTotal(intent.amount);
				intent.beneficiary.getBankAccount().getBalance().increaseTotal(intent.amount);
				
				Transaction transaction = new Transaction(
						intent.amount, 
						intent.beneficiary.getBankAccount(), 
						intent.source.getBankAccount(), 
						"Successfully made transaction");
				
				transactionRepo.save(transaction);
				System.out.println("Transaction saved to repo");
				return transaction;
			}else {
				throw new TransactionServiceException("Transaction intent is not valid. Transaction not made!");
			}
		}catch(Exception e) {
			throw new TransactionServiceException("Cannot process intent, error: " + e.getMessage());
		}
		
	}
	
	public AccountTransactions getTransactionsOf(BankAccount account) throws TransactionServiceException {
		return new AccountTransactions(
				getDebitTransactionsOf(account),
				getCreditTransactionsOf(account));
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
