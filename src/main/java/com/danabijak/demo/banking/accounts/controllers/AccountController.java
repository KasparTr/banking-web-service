package com.danabijak.demo.banking.accounts.controllers;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.danabijak.demo.banking.accounts.http.AccountBalanceResponse;
import com.danabijak.demo.banking.accounts.http.AccountStatementClientResponse;
import com.danabijak.demo.banking.accounts.services.AccountService;

@RestController
public class AccountController {
	@Autowired
	private AccountService accountService;
	
	@GetMapping(value="/services/accounts/{id}/balance")
	public CompletableFuture<ResponseEntity<AccountBalanceResponse>> getAccountBalance(@PathVariable long id){
		// TODO: IMPLEMENT CHECK FOR TOKEN VS USER ID!!
		return accountService.getBankAccount(id).thenApply(account -> {
			return ResponseEntity.ok(new AccountBalanceResponse(account));
		});
	}
	
	@GetMapping("/services/accounts/{id}/statement")
	public CompletableFuture<ResponseEntity<AccountStatementClientResponse>> getAccountStatement(@PathVariable long id){
		// TODO: IMPLEMENT CHECK FOR TOKEN VS USER ID!!		
		return accountService.getAccountStatement(id).thenApply(statement -> {
			return ResponseEntity.ok(statement);
		});			
	}
	
}
