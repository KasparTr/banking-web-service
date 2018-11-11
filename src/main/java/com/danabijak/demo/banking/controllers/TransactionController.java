package com.danabijak.demo.banking.controllers;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

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
import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.model.DepositClientRequest;
import com.danabijak.demo.banking.model.TransactionIntentBuilder;
import com.danabijak.demo.banking.services.UserService;
import com.danabijak.demo.banking.transactions.factories.TransactionIntentFactory;
import com.danabijak.demo.banking.transactions.http.TransactionIntentClientRequest;
import com.danabijak.demo.banking.transactions.http.TransactionIntentClientResponse;
import com.danabijak.demo.banking.transactions.services.TransactionIntentService;
import com.danabijak.demo.banking.transactions.services.TransactionService;

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
	

	@GetMapping("/services/transactions/{id}")
	public Transaction findById(@PathVariable long id){
		
		return transactionService.findTransactionBy(id);
	}
	
	@PostMapping("/services/transactions/deposit")
	public ResponseEntity<TransactionIntentClientResponse> deposit(@Valid @RequestBody DepositClientRequest request) {
		TransactionIntentFactory factory = new TransactionIntentFactory();
		
		
		User bank = userService.findByUsername("bankItself@bank.com");
		User beneficiary = userService.find(request.depositor.id);
	
		TransactionIntent intent = factory.createDepositRequest(bank, beneficiary, request);
		System.out.println("TransactionController | depositTo() | intent created: " + intent.toString());
		

		TransactionIntent publishedIntent = depositIntentService.attemptPublish(intent);
		
		System.out.println("TransactionController | depositTo() | intent published: " + publishedIntent.toString());

		// should be end of it, but right now instead of PUB/SUB we send directly to:
		// transactionService.processDeposit(intent)
		System.out.println("TransactionController | depositTo() | transactionService: " + transactionService);
		transactionService.porcess(publishedIntent);
		System.out.println("TransactionController | depositTo() | intent processed");

		
		TransactionIntentClientResponse response = new TransactionIntentClientResponse(publishedIntent.isValid(), "Intent Published", publishedIntent);
		return ResponseEntity.ok(response);
		
	}	
	
	// TODO: NOT FINISHED
	@PostMapping("/services/transactions/intent")
	public ResponseEntity<TransactionIntentClientResponse> createIntent(@Valid @RequestBody TransactionIntentClientRequest request) {
		TransactionIntentFactory factory = new TransactionIntentFactory();
		
		User source = userService.find(request.source.id);
		User beneficiary = userService.find(request.beneficiary.id);
		TransactionIntent intent = factory.createFromClientRequest(source, beneficiary, request);
		TransactionIntent publishedIntent =  withdrawIntentService.attemptPublish(intent);
		
		
		TransactionIntentClientResponse response = new TransactionIntentClientResponse(publishedIntent.isValid(), "Intent Published", publishedIntent);
		return ResponseEntity.ok(response);
		
	}	
}

