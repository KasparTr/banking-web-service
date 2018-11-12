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
import com.danabijak.demo.banking.transactions.services.TransactionIntentService;
import com.danabijak.demo.banking.transactions.services.TransactionService;
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
	private TransactionService transactionService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TransactionIntentFactory transactionIntentFactory;
	

	@GetMapping("/services/transactions/{id}")
	public Transaction findById(@PathVariable long id){
		
		return transactionService.findTransactionBy(id);
	}
	
	
	//TODO: Enable multiple account support
	@PostMapping("/services/transactions/deposit")
	public CompletableFuture<ResponseEntity<TransactionIntentClientResponse>> deposit(@Valid @RequestBody TransactionClientRequest request) {
		
		CompletableFuture<TransactionIntent> depositIntentFuture = 
				transactionIntentFactory.createDepositIntent(
						request.entity.id,
						Money.of(CurrencyUnit.of(request.money.currency), request.money.amount));
				
				
		return depositIntentFuture.thenApply(intent -> {
			System.out.println("TransactionController | depositIntentFuture resolved, intent: " + intent.toString());
			TransactionIntent publishedIntent = depositIntentService.attemptPublish(intent);
			System.out.println("TransactionController | intent publised");

			// return publishedIntent
			// should be end of handler, instead currently, instead of publishing to channel, we send directly to TransactionService
			transactionService.porcess(publishedIntent);
			System.out.println("TransactionController | intent processed");

			
			TransactionIntentClientResponse response = new TransactionIntentClientResponse(publishedIntent.isValid(), "Intent Published", publishedIntent);
			return ResponseEntity.ok(response); 
//		    
//		    return intent; 
			//ResponseEntity<TransactionIntentClientResponse>
		});

	}
	
//	//TODO: Enable multiple account support
//	@PostMapping("/services/transactions/withdraw")
//	public ResponseEntity<TransactionIntentClientResponse> withdraw(@Valid @RequestBody TransactionClientRequest request) {
//		TransactionIntentFactory factory = new TransactionIntentFactory();
//		
//		User bank = userService.findByUsername("bankItself@bank.com");
//		User user = userService.find(request.entity.id);
//	
//		
//		TransactionIntent intent = factory.create(user, bank, request);		
//		TransactionIntent publishedIntent = withdrawIntentService.attemptPublish(intent);
//		
//		// should be end of it, but right now instead of PUB/SUB we send directly to:
//		// transactionService.processDeposit(intent)
//		transactionService.porcess(publishedIntent);
//		
//		TransactionIntentClientResponse response = new TransactionIntentClientResponse(publishedIntent.isValid(), "Intent Published", publishedIntent);
//		return ResponseEntity.ok(response);
//		
//	}	

}

