package com.danabijak.demo.banking.transactions.services;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import javax.transaction.Transactional;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.transactions.exceptions.TransactionIntentPublishException;
import com.danabijak.demo.banking.transactions.http.TransactionIntentClientResponse;
import com.danabijak.demo.banking.transactions.model.TransactionClientRequest;

/**
 * TransactionIntentService is a service that publishes transaction intents into a message channel (using PUB/SUB or other).
 * This service is responsible for making sure the intents are valid, publish and and report their publish status.
 * TODO: IMPLEMENTATION MUST BE ASYNC!!
 * @author kaspar
 *
 */
@Service
public interface TransactionIntentService {
	
	/**
	 * Publish transaction intent to the transactions pool if intent is valid.
	 * Returns a new published intent object that has been changed from the argument intent .
	 * @param intent - Intent draft for a future transaction.
	 * @return - Published intent that has been changed from the argument intent
	 * @throws TransactionIntentPublishException - if something goes horribly wrong here.
	 */
	@Async("asyncExecutor")
	public CompletableFuture<TransactionIntent> publish(TransactionIntent intent) throws TransactionIntentPublishException;
	
	//@Async("asyncExecutor")
	//public CompletableFuture<TransactionIntent> publishIntent(TransactionClientRequest intentRequest) throws TransactionIntentPublishException;
}
