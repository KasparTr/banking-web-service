package com.danabijak.demo.banking.domain.transactions.services;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danabijak.demo.banking.domain.accounts.entity.BankAccount;
import com.danabijak.demo.banking.domain.accounts.repositories.AccountRepository;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntent;
import com.danabijak.demo.banking.domain.transactions.exceptions.TransactionServiceException;

@Service
@Resource(name="withdrawService")
public class WithdrawService extends TransactionServiceImpl{
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Override
	protected void updateBalances(TransactionIntent intent) throws TransactionServiceException{

		Optional<BankAccount> sourceAccount = accountRepository.findById(intent.source.getBankAccount().getId());		
		
		if(sourceAccount.isPresent()) {
			sourceAccount.get().decreaseBalance(intent.amount);
			accountRepository.save(sourceAccount.get());
		}else {
			throw new TransactionServiceException("Cannot find source bank account");
		}

	}
	

}
