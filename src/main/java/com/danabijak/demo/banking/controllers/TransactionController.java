package com.danabijak.demo.banking.controllers;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.danabijak.demo.banking.factories.TransactionIntentFactory;
import com.danabijak.demo.banking.model.DepositClientRequest;
import com.danabijak.demo.banking.model.TransactionIntentBuilder;
import com.danabijak.demo.banking.model.TransactionIntentClientRequest;
import com.danabijak.demo.banking.model.TransactionIntentPublishAttemptReport;
import com.danabijak.demo.banking.services.TransactionIntentService;
import com.danabijak.demo.banking.services.TransactionService;
import com.danabijak.demo.banking.services.UserService;

@RestController
public class TransactionController {

	
	@Autowired
	private TransactionIntentService transactionIntentService;
	
	@Autowired
	private TransactionService transactionService;
	
	
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
	public ResponseEntity<TransactionIntent> depositTo(@Valid @RequestBody DepositClientRequest request) {
		TransactionIntentFactory factory = new TransactionIntentFactory();
		TransactionIntent intent = factory.createDepositRequest(request);
		TransactionIntentPublishAttemptReport attemptReport = transactionIntentService.attemptToPublishIntent(intent);
		
		return ResponseEntity.ok(intent);
		
	}	
	
	// TODO: NOT FINISHED
	@PostMapping("/services/transactions/intent")
	public ResponseEntity<TransactionIntent> createIntent(@Valid @RequestBody TransactionIntentClientRequest intentRequest) {
		TransactionIntentFactory factory = new TransactionIntentFactory();
		TransactionIntent intent = factory.createFromClientRequest(intentRequest);
		TransactionIntentPublishAttemptReport attemptReport = transactionIntentService.attemptToPublishIntent(intent);
		
		
		return ResponseEntity.ok(intent);
		
	}	
}

