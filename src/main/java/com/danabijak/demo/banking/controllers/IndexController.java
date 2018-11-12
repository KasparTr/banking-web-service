package com.danabijak.demo.banking.controllers;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danabijak.demo.banking.accounts.repositories.AccountRepository;
import com.danabijak.demo.banking.entity.BankAccount;
import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.transactions.exceptions.BankAccountException;
import com.danabijak.demo.banking.transactions.http.AccountBalanceResponse;
import com.danabijak.demo.banking.transactions.http.BankAccountStatementClientResponse;

@RestController
public class IndexController {
	@Autowired
	private AccountRepository accountRepository;

	
	@GetMapping(value="/")
	public String index() {
		return "Hello";
	}
	
	@GetMapping(value="/services")
	public String services() {
		return "This is Protected Services Area";
	}
	
	@GetMapping(value="/services/accounts/{id}/balance")
	public ResponseEntity<AccountBalanceResponse> getAccountBalance(@PathVariable long id){
		Optional<BankAccount> account = accountRepository.findById(id);
		
		if(account.isPresent()) {
			return ResponseEntity.ok(new AccountBalanceResponse(account.get()));
		}else {
			throw new BankAccountException("Not bank account found with id: " + id);
		}
	}
	
}
