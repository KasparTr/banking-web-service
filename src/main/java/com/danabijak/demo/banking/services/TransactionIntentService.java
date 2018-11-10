package com.danabijak.demo.banking.services;

import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.exceptions.TransactionIntentPublishException;
import com.danabijak.demo.banking.model.TransactionIntentPublishAttemptReport;

/**
 * TransactionIntentService is a service that publishes transaction intents into a message channel (using PUB/SUB or other).
 * This service is responsible for making sure the intents are valid, publish and and report their publish status.
 * TODO: IMPLEMENTATION MUST BE ASYNC!!
 * @author kaspar
 *
 */
public interface TransactionIntentService {
	
	/**
	 * Publish an intent for transaction to the transactions pool.
	 * @param intent - Intent for a future transaction.
	 * @return - Report describes the publishing status of the intent.
	 * @throws TransactionIntentPublishException - if something goes horribly wrong here.
	 */
	public TransactionIntentPublishAttemptReport attemptToPublishIntent(TransactionIntent intent) throws TransactionIntentPublishException;
}
