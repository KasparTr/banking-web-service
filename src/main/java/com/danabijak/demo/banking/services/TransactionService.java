package com.danabijak.demo.banking.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.danabijak.demo.banking.entity.Transaction;
import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.entity.TransactionalEntity;
import com.danabijak.demo.banking.exceptions.TransactionServiceException;

@Component
public interface TransactionService {
	public Transaction porcess(TransactionIntent intent) throws TransactionServiceException;
	
	
	public List<Transaction> getAllTransactionsOf(long id) throws TransactionServiceException;
	
	public Transaction findTransactionBy(long id) throws TransactionServiceException;

}
