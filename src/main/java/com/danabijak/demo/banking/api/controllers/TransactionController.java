package com.danabijak.demo.banking.api.controllers;


import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.danabijak.demo.banking.domain.transactions.valueobjects.TransactionRequest;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntent;
import com.danabijak.demo.banking.domain.transactions.factories.TransactionIntentFactory;
import com.danabijak.demo.banking.domain.transactions.services.TransactionIntentService;
import com.danabijak.demo.banking.domain.transactions.services.TransactionService;
import com.danabijak.demo.banking.domain.transactions.valueobjects.TransactionIntentResponse;

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
	public CompletableFuture<ResponseEntity<TransactionIntentResponse>> deposit(@Valid @RequestBody TransactionRequest request) {
		
		CompletableFuture<TransactionIntent> intentFuture = 
				transactionIntentFactory.createDepositIntent(
						request.entity.id,
						Money.of(CurrencyUnit.of(request.money.currency), request.money.amount));
		
		return intentFuture.thenCompose(intent -> {	
			CompletableFuture<TransactionIntent> publishedIntentFuture = depositIntentService.publish(intent);
			return publishedIntentFuture.thenApply(publishedIntent -> {
				depositService.porcess(intent);
				
				TransactionIntentResponse response = new TransactionIntentResponse(publishedIntent.isValid(), "Intent Published", publishedIntent);
				return ResponseEntity.ok(response); 
			});
			
		});

	}
	
	//TODO: Enable multiple account support
	@PostMapping("/services/transactions/withdraw")
	public CompletableFuture<ResponseEntity<TransactionIntentResponse>> withdraw(@Valid @RequestBody TransactionRequest request) {
		CompletableFuture<TransactionIntent> intentFuture = 
				transactionIntentFactory.createWithdrawIntent(
						request.entity.id,
						Money.of(CurrencyUnit.of(request.money.currency), request.money.amount));
		
		return intentFuture.thenCompose(intent -> {	
			CompletableFuture<TransactionIntent> publishedIntentFuture = withdrawIntentService.publish(intent);
			return publishedIntentFuture.thenApply(publishedIntent -> {
				withdrawService.porcess(intent);
				
				TransactionIntentResponse response = new TransactionIntentResponse(publishedIntent.isValid(), "Intent Published", publishedIntent);
				return ResponseEntity.ok(response); 
			});
			
		});
		
	}	

}

