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
import com.danabijak.demo.banking.model.TransactionIntentClientRequest;
import com.danabijak.demo.banking.model.TransactionIntentPublishAttemptReport;
import com.danabijak.demo.banking.services.TransactionIntentService;
import com.danabijak.demo.banking.services.TransactionService;
import com.danabijak.demo.banking.services.UserService;
import com.danabijak.demo.banking.transactions.factories.TransactionIntentFactory;
import com.danabijak.demo.banking.transactions.http.responses.TransactionIntentClientResponse;

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
	
	
	@GetMapping("/services/transactions/all/{entityId}")
	public List<Transaction> getAllTransactionsOf(@PathVariable long entityId){
		// if access_token matches the entityId
		return transactionService.getAllTransactionsOf(entityId);
	}
	
	@GetMapping("/services/transactions/{id}")
	public Transaction findById(@PathVariable long id){
		
		return transactionService.findTransactionBy(id);
	}
	
	@PostMapping("/services/transactions/deposit")
	public ResponseEntity<TransactionIntentPublishAttemptReport> depositTo(@Valid @RequestBody DepositClientRequest request) {
		TransactionIntentFactory factory = new TransactionIntentFactory();
		
		
		User bank = userService.findByUsername("bankItself@bank.com");
		User beneficiary = userService.find(request.depositor.id);
	
		TransactionIntent intent = factory.createDepositRequest(bank, beneficiary, request);
		System.out.println("intent: " + intent);
		
		TransactionIntentPublishAttemptReport attemptReport = depositIntentService.attemptToPublishIntent(intent);
		
		return ResponseEntity.ok(attemptReport);
		
	}	
	
	// TODO: NOT FINISHED
	@PostMapping("/services/transactions/intent")
	public ResponseEntity<TransactionIntentPublishAttemptReport> createIntent(@Valid @RequestBody TransactionIntentClientRequest request) {
		TransactionIntentFactory factory = new TransactionIntentFactory();
		
		User source = userService.find(request.source.id);
		User beneficiary = userService.find(request.beneficiary.id);
		TransactionIntent intent = factory.createFromClientRequest(source, beneficiary, request);
		TransactionIntentPublishAttemptReport attemptReport = withdrawIntentService.attemptToPublishIntent(intent);
		
		
		return ResponseEntity.ok(attemptReport);
		
	}	
}

