package com.danabijak.demo.banking.domain.transactions.services;


import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntent;
import com.danabijak.demo.banking.domain.transactions.exceptions.TransactionIntentException;

/**
 * TransactionIntentService is a service that publishes transaction intents into a message channel.
 * This service is responsible for making sure the intents are valid, publish and their report their publish status.
 * TODO: NB! Currently the publish methods call the TransactionService directly because the PUB/SUB pattern is not implemented.
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
	 * @throws TransactionIntentException - if something goes horribly wrong here.
	 */
	@Async("asyncExecutor")
	public CompletableFuture<TransactionIntent> publish(TransactionIntent intent) throws TransactionIntentException;
	
}
