package com.danabijak.demo.banking.transactions.controllers;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Currency;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.danabijak.demo.banking.accounts.services.AccountService;
import com.danabijak.demo.banking.entity.Transaction;
import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.entity.TransactionIntentStatus;
import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.entity.TransactionIntentStatus.TRANSFER_STATUS;
import com.danabijak.demo.banking.transactions.factories.TransactionIntentFactory;
import com.danabijak.demo.banking.transactions.http.TransactionIntentClientRequest;
import com.danabijak.demo.banking.transactions.http.TransactionIntentClientResponse;
import com.danabijak.demo.banking.transactions.model.TransactionClientRequest;
import com.danabijak.demo.banking.transactions.model.TransactionIntentBuilder;
import com.danabijak.demo.banking.transactions.services.DepositIntentService;
import com.danabijak.demo.banking.transactions.services.TransactionIntentService;
import com.danabijak.demo.banking.transactions.services.TransactionIntentServiceImpl;
import com.danabijak.demo.banking.transactions.services.TransactionService;
import com.danabijak.demo.banking.transactions.services.WithdrawIntentService;
import com.danabijak.demo.banking.users.services.UserService;

@RestController
public class TransactionController {

	
	@Autowired
	@Qualifier("depositIntentService")
	private TransactionIntentService depositIntentService;
	
	@Autowired
	@Qualifier("withdrawIntentService")
	private TransactionIntentService withdrawIntentService;
	
	@Autowired
	@Qualifier("withdrawService")
	private TransactionService withdrawService;
	
	@Autowired
	@Qualifier("depositService")
	private TransactionService depositService;
	
	@Autowired
	private TransactionIntentFactory transactionIntentFactory;
		
	
	//TODO: Enable multiple account support
	@PostMapping("/services/transactions/deposit")
	public CompletableFuture<Object> deposit(@Valid @RequestBody TransactionClientRequest request) {
		
		CompletableFuture<TransactionIntent> intentFuture = 
				transactionIntentFactory.createDepositIntent(
						request.entity.id,
						Money.of(CurrencyUnit.of(request.money.currency), request.money.amount));
		
		return intentFuture.thenApply(intent -> {	
			CompletableFuture<TransactionIntent> publishedIntentFuture = depositIntentService.publish(intent);
			return publishedIntentFuture.thenApply(publishedIntent -> {
				depositService.porcess(intent);
				
				TransactionIntentClientResponse response = new TransactionIntentClientResponse(publishedIntent.isValid(), "Intent Published", publishedIntent);
				return ResponseEntity.ok(response); 
			});
			
		});

	}
	
	//TODO: Enable multiple account support
	@PostMapping("/services/transactions/withdraw")
	public CompletableFuture<Object> withdraw(@Valid @RequestBody TransactionClientRequest request) {
		CompletableFuture<TransactionIntent> intentFuture = 
				transactionIntentFactory.createWithdrawIntent(
						request.entity.id,
						Money.of(CurrencyUnit.of(request.money.currency), request.money.amount));
		
		return intentFuture.thenApply(intent -> {	
			CompletableFuture<TransactionIntent> publishedIntentFuture = withdrawIntentService.publish(intent);
			return publishedIntentFuture.thenApply(publishedIntent -> {
				withdrawService.porcess(intent);
				
				TransactionIntentClientResponse response = new TransactionIntentClientResponse(publishedIntent.isValid(), "Intent Published", publishedIntent);
				return ResponseEntity.ok(response); 
			});
			
		});
		
	}	

}

