package com.danabijak.demo.banking.services;

import com.danabijak.demo.banking.entity.Transaction;
import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.exceptions.TransactionServiceExcetion;

public interface TransactionService {
	public Transaction porcessTransactionIntent(TransactionIntent intent) throws TransactionServiceExcetion;

}
