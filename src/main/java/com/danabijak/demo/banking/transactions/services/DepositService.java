package com.danabijak.demo.banking.transactions.services;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danabijak.demo.banking.accounts.repositories.AccountRepository;
import com.danabijak.demo.banking.entity.BankAccount;
import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.transactions.exceptions.TransactionServiceException;


@Service
@Resource(name="depositService")
public class DepositService extends TransactionServiceImpl{
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Override
	protected void updateBalances(TransactionIntent intent) throws TransactionServiceException{

		//Optional<BankAccount> sourceAccount = accountRepository.findById(intent.source.getBankAccount().getId());		
		Optional<BankAccount> beneAccount = accountRepository.findById(intent.beneficiary.getBankAccount().getId());
		
		if(beneAccount.isPresent()) {
			beneAccount.get().increaseBalance(intent.amount);
			accountRepository.save(beneAccount.get());
		}else {
			throw new TransactionServiceException("Cannot find bank account");
		}

	}
	

}