package com.danabijak.demo.banking.transactions.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.danabijak.demo.banking.accounts.repositories.AccountRepository;
import com.danabijak.demo.banking.entity.BankAccount;
import com.danabijak.demo.banking.entity.Transaction;
import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.entity.TransactionIntentStatus;
import com.danabijak.demo.banking.entity.TransactionalEntity;
import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.entity.TransactionIntentStatus.TRANSFER_STATUS;
import com.danabijak.demo.banking.infra.repositories.TransactionIntentRepository;
import com.danabijak.demo.banking.infra.repositories.TransactionRepository;
import com.danabijak.demo.banking.transactions.exceptions.TransactionNotFoundException;
import com.danabijak.demo.banking.transactions.exceptions.TransactionServiceException;
import com.danabijak.demo.banking.transactions.model.AccountTransactions;
import com.danabijak.demo.banking.transactions.model.TransactionIntentBuilder;
import com.danabijak.demo.banking.users.exceptions.UserNotFoundException;
import com.danabijak.demo.banking.users.services.UserService;

@Component
public class TransactionServiceImpl implements TransactionService{
	
	@Autowired
	private TransactionRepository transactionRepo;
	
	@Autowired
	private TransactionIntentRepository transactionIntentRepo;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Override
	public void porcessAllIntents() throws TransactionServiceException {
		List<TransactionIntent> allIntents = transactionIntentRepo.findAll();
		for(TransactionIntent i:allIntents) {
			if(!i.isPaid()) {
				process(i);
			}
		}
	}

	private Transaction process(TransactionIntent intent) throws TransactionServiceException{
		
		Optional<BankAccount> sourceAccount = accountRepository.findById(intent.source.getBankAccount().getId());		
		Optional<BankAccount> beneAccount = accountRepository.findById(intent.beneficiary.getBankAccount().getId());
		
		if(sourceAccount.isPresent() && beneAccount.isPresent()) {

			sourceAccount.get().decreaseBalance(intent.amount);
			beneAccount.get().increaseBalance(intent.amount);
			
			accountRepository.save(sourceAccount.get());
			accountRepository.save(beneAccount.get());
		}else {
			throw new TransactionServiceException("Cannot find intendad bank accounts");
		}
		
		Transaction transaction = new Transaction(
				intent.amount, 
				intent.beneficiary.getBankAccount(), 
				intent.source.getBankAccount(), 
				"Successfully made transaction");
		
		transactionRepo.save(transaction);
		intent.setPaidTo(true);
		return transaction;
	}
	
	@Override
	public Transaction porcess(TransactionIntent intent) throws TransactionServiceException {

		try {
			if(intent.isValid()) {				
				return process(intent);
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
