package com.danabijak.demo.banking.accounts.controllers;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danabijak.demo.banking.accounts.exceptions.BankAccountException;
import com.danabijak.demo.banking.accounts.http.AccountBalanceResponse;
import com.danabijak.demo.banking.accounts.http.AccountStatementClientResponse;
import com.danabijak.demo.banking.accounts.repositories.AccountRepository;
import com.danabijak.demo.banking.accounts.services.AccountService;
import com.danabijak.demo.banking.entity.BankAccount;
import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.users.factories.BankAccountStatementFactory;
import com.danabijak.demo.banking.users.services.UserService;

@RestController
public class AccountController {
	@Autowired
	private AccountService accountService;
	
	@GetMapping(value="/services/accounts/{id}/balance")
	public ResponseEntity<AccountBalanceResponse> getAccountBalance(@PathVariable long id){
		BankAccount account = accountService.getBankAccount(id);
		return ResponseEntity.ok(new AccountBalanceResponse(account));
	}
	
//	@GetMapping("/services/accounts/{id}/balance")
//	public CompletableFuture<ResponseEntity<AccountBalanceResponse>> getAccountBalance(@PathVariable long id, @PathVariable long accountId){
//		// TODO: IMPLEMENT CHECK FOR TOKEN VS USER ID!!
//		CompletableFuture<User> userFuture = userService.find(id);
//		
//		return userFuture.thenApply(user -> {
//			// find account. Since currency user only has 1 account a search here is not required.
//			BankAccount correctAccount = user.getBankAccount();
//
//			return ResponseEntity.ok(new AccountBalanceResponse(correctAccount));
//		});
//		
//	}
	
}
