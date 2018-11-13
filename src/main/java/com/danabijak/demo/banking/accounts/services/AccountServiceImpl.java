package com.danabijak.demo.banking.accounts.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.danabijak.demo.banking.accounts.exceptions.BankAccountException;
import com.danabijak.demo.banking.accounts.http.AccountBalanceResponse;
import com.danabijak.demo.banking.accounts.repositories.AccountRepository;
import com.danabijak.demo.banking.entity.BankAccount;
import com.danabijak.demo.banking.entity.Transaction;
import com.danabijak.demo.banking.infra.repositories.TransactionRepository;
import com.danabijak.demo.banking.transactions.exceptions.TransactionNotFoundException;
import com.danabijak.demo.banking.transactions.exceptions.TransactionServiceException;
import com.danabijak.demo.banking.transactions.model.AccountTransactions;

@Component
public class AccountServiceImpl implements AccountService{
	
	@Autowired
	private TransactionRepository transactionRepo;
	
	@Autowired
	private AccountRepository accountRepository;
	

	public AccountTransactions getTransactionsOf(BankAccount account) throws BankAccountException {
		return new AccountTransactions(
				getDebitTransactionsOf(account),
				getCreditTransactionsOf(account));
	}
	
	
	
	@Override
	public List<Transaction> getDebitTransactionsOf(BankAccount account) throws BankAccountException {
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
	public List<Transaction> getCreditTransactionsOf(BankAccount account) throws BankAccountException {
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
	public BankAccount getBankAccount(long id) throws BankAccountException {
		Optional<BankAccount> account = accountRepository.findById(id);
		
		if(account.isPresent()) {
			return account.get();
		}else {
			throw new BankAccountException("Not bank account found with id: " + id);
		}
	}	

}
