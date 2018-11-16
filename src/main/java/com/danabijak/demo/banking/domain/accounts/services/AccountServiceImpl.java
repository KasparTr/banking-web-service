package com.danabijak.demo.banking.domain.accounts.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.danabijak.demo.banking.domain.accounts.entity.BankAccount;
import com.danabijak.demo.banking.domain.accounts.exceptions.BankAccountException;
import com.danabijak.demo.banking.domain.accounts.repositories.AccountRepository;
import com.danabijak.demo.banking.domain.accounts.valueobjects.AccountStatementResponse;
import com.danabijak.demo.banking.domain.accounts.valueobjects.AccountTransactions;
import com.danabijak.demo.banking.domain.transactions.entity.Transaction;
import com.danabijak.demo.banking.domain.transactions.repositories.TransactionRepository;
import com.danabijak.demo.banking.domain.users.factories.BankAccountStatementFactory;

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
			throw new BankAccountException("No bank account found with id: " + id);
		}
	}



	@Override
	@Async("asyncExecutor")
	public CompletableFuture<AccountStatementResponse> getAccountStatement(long id) throws BankAccountException {
		CompletableFuture<BankAccount> accountFuture = getBankAccount(id);
		
		return accountFuture.thenCompose(account -> {
			CompletableFuture<AccountTransactions> transactionsFuture = getTransactionsOf(account);
			
			return transactionsFuture.thenApply(transactions -> {
				AccountStatementResponse statement = baStatementFactory.generateStatement(
						account, 
						transactions);

				return statement;
			});
			
			
		});
	}	

}
