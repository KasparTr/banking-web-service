package com.danabijak.demo.banking.accounts.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.danabijak.demo.banking.accounts.exceptions.BankAccountException;
import com.danabijak.demo.banking.accounts.http.AccountBalanceResponse;
import com.danabijak.demo.banking.accounts.http.AccountStatementClientResponse;
import com.danabijak.demo.banking.accounts.repositories.AccountRepository;
import com.danabijak.demo.banking.entity.BankAccount;
import com.danabijak.demo.banking.entity.Transaction;
import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.entity.TransactionIntentStatus;
import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.entity.TransactionIntentStatus.TRANSFER_STATUS;
import com.danabijak.demo.banking.infra.repositories.TransactionRepository;
import com.danabijak.demo.banking.transactions.exceptions.TransactionNotFoundException;
import com.danabijak.demo.banking.transactions.exceptions.TransactionServiceException;
import com.danabijak.demo.banking.transactions.model.AccountTransactions;
import com.danabijak.demo.banking.transactions.model.TransactionIntentBuilder;
import com.danabijak.demo.banking.users.factories.BankAccountStatementFactory;

@Service
public class AccountServiceImpl implements AccountService{
	
	@Autowired
	private TransactionRepository transactionRepo;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private BankAccountStatementFactory baStatementFactory;
	
	
	@Override
	@Async("asyncExecutor")
	public CompletableFuture<AccountTransactions> getTransactionsOf(BankAccount account) throws BankAccountException {
		CompletableFuture<List<Transaction>> debitTransactions = getDebitTransactionsOf(account);
		CompletableFuture<List<Transaction>> creditTransactions = getCreditTransactionsOf(account);
		
		CompletableFuture<Void> allTransactionFutures = CompletableFuture.allOf(debitTransactions, creditTransactions);
		
		return allTransactionFutures.thenApply(it -> {
			List<Transaction> debits = debitTransactions.join();
		    List<Transaction> credits = creditTransactions.join();
		    
			return new AccountTransactions(debits, credits);
		});
		
	}
	
	
	
	@Override
	@Async("asyncExecutor")
	public  CompletableFuture<List<Transaction>> getDebitTransactionsOf(BankAccount account) throws BankAccountException {
		// TODO: only return transactions that the id parameter is the source!
		List<Transaction> allTransactions = transactionRepo.findAll();
		List<Transaction> accountTransactions = new ArrayList<>();
		
		//TODO: Change this loop for smarter Repository find method!
		for(Transaction trans:allTransactions) {
			if(trans.sourceAccount.getId() == account.getId()) {
				accountTransactions.add(trans);
			}
		}
		return CompletableFuture.completedFuture(accountTransactions);
	}
	
	@Override
	@Async("asyncExecutor")
	public CompletableFuture<List<Transaction>> getCreditTransactionsOf(BankAccount account) throws BankAccountException {
		// TODO: only return transactions that the id parameter is the beneficiary
		List<Transaction> allTransactions = transactionRepo.findAll();
		List<Transaction> accountTransactions = new ArrayList<>();
		
		//TODO: Change this loop for smarter Repository find method!
		for(Transaction trans:allTransactions) {
			if(trans.beneficiaryAccount.getId() == account.getId()) {
				accountTransactions.add(trans);
			}
		}
		return CompletableFuture.completedFuture(accountTransactions);
	}



	@Override
	@Async("asyncExecutor")
	public CompletableFuture<BankAccount> getBankAccount(long id) throws BankAccountException {
		Optional<BankAccount> account = accountRepository.findById(id);
		
		if(account.isPresent()) {
			return CompletableFuture.completedFuture(account.get());
		}else {
			throw new BankAccountException("Not bank account found with id: " + id);
		}
	}



	@Override
	@Async("asyncExecutor")
	public CompletableFuture<AccountStatementClientResponse> getAccountStatement(long id) throws BankAccountException {
		CompletableFuture<BankAccount> accountFuture = getBankAccount(id);
		
		return accountFuture.thenCompose(account -> {
			CompletableFuture<AccountTransactions> transactionsFuture = getTransactionsOf(account);
			
			return transactionsFuture.thenApply(transactions -> {
				AccountStatementClientResponse statement = baStatementFactory.generateStatement(
						account, 
						transactions);

				return statement;
			});
			
			
		});
	}	

}
