package com.danabijak.demo.banking.accounts.services;

import java.util.List;

import org.springframework.stereotype.Component;

import com.danabijak.demo.banking.accounts.exceptions.BankAccountException;
import com.danabijak.demo.banking.entity.BankAccount;
import com.danabijak.demo.banking.entity.Transaction;
import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.transactions.exceptions.TransactionServiceException;
import com.danabijak.demo.banking.transactions.model.AccountTransactions;


@Component
public interface AccountService {
	
	/**
	 * Get all debit transactions of the specified bank account.
	 * @param account
	 * @return
	 * @throws TransactionServiceException
	 */
	public List<Transaction> getDebitTransactionsOf(BankAccount account) throws BankAccountException;
	
	/**
	 * Get all credit transaction of the specified bank account
	 * @param account
	 * @return
	 * @throws TransactionServiceException
	 */
	public List<Transaction> getCreditTransactionsOf(BankAccount account) throws BankAccountException;
	
	/**
	 * Get all transaction of the specified bank account
	 * @param account
	 * @return
	 * @throws TransactionServiceException
	 */
	public AccountTransactions getTransactionsOf(BankAccount account) throws BankAccountException;
	
	/**
	 * Get all transaction of the specified bank account
	 * @param account
	 * @return
	 * @throws TransactionServiceException
	 */
	public BankAccount getBankAccount(long id) throws BankAccountException;

}
