package com.danabijak.demo.banking.transactions.services;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.danabijak.demo.banking.infra.repositories.TransactionIntentRepository;
import com.danabijak.demo.banking.infra.repositories.UserRepository;
import com.danabijak.demo.banking.transactions.entity.TransactionIntent;
import com.danabijak.demo.banking.transactions.entity.TransactionIntentStatus;
import com.danabijak.demo.banking.transactions.entity.TransactionalEntity;
import com.danabijak.demo.banking.transactions.entity.TransactionIntentStatus.TRANSFER_STATUS;
import com.danabijak.demo.banking.transactions.exceptions.TransactionIntentPublishException;
import com.danabijak.demo.banking.transactions.http.TransactionIntentClientResponse;
import com.danabijak.demo.banking.transactions.model.TransactionClientRequest;
import com.danabijak.demo.banking.transactions.model.TransactionIntentBuilder;
import com.danabijak.demo.banking.transactions.model.ValidationReport;
import com.danabijak.demo.banking.users.entity.User;
import com.danabijak.demo.banking.users.services.UserService;

@Service
public abstract class TransactionIntentServiceImpl implements TransactionIntentService{

	@Autowired
	private TransactionIntentRepository transactionIntentRepo;
	
	@Override
	@Async("asyncExecutor")
	public CompletableFuture<TransactionIntent> publish(TransactionIntent intent) throws TransactionIntentPublishException {
	    ValidationReport validationReport = validateIntent(intent);
		
		if(validationReport.valid) {
			transactionIntentRepo.save(intent);
			reserverParticipantsBalance(intent);
			publishToChannel(intent);
			CompletableFuture<TransactionIntent> completableFuture  = new CompletableFuture<>();
			completableFuture.complete(intent);
			return completableFuture;
		}else {
			throw new TransactionIntentPublishException("Transaction intent not publised. Errors: " + validationReport.generateStringMessage());
		}
	}
	
//	@Override
//	@Async("asyncExecutor")
//	public CompletableFuture<TransactionIntent> publishIntent(TransactionClientRequest request) throws TransactionIntentPublishException {
//		
//		CompletableFuture<User> bank = userService.findByUsername("bankItself@bank.com");
//		CompletableFuture<User> user = userService.find(request.entity.id);
//		Money money = Money.of(CurrencyUnit.of(request.money.currency), request.money.amount);
//		
//		CompletableFuture<Void> allUserFutures = CompletableFuture.allOf(bank, user);
//		
//		return allUserFutures.thenApply(it -> {
//		    User bUser = bank.join();
//		    User uUser = user.join();
//		    
//		    TransactionIntent intent = makeTransactionIntent(uUser, bUser, money);
//		    
//		    ValidationReport validationReport = validateIntent(intent);
//			
//			if(validationReport.valid) {
//				transactionIntentRepo.save(intent);
//				reserverParticipantsBalance(intent);
//				return publish(intent);
//			}else {
//				throw new TransactionIntentPublishException("Transaction intent not publised. Errors: " + validationReport.generateStringMessage());
//			}
//		});
//	}
	

	/**
	 * NB! Publishing intents to messaging channel is not implement yet.
	 * TODO: Instead of directly sending intent to TransactionSerice for processing, publish the intent to the intent pool and 
	 * 	have the TransactionService (as a subscriber) process the intents.
	 */
	protected void publishToChannel(TransactionIntent intent) {
		// TODO: Implement publishing to PUB/SUB channel. 
		// NB! if that fails keep in mind to undo reserver balance/limits!
		
	}

	protected abstract TransactionIntent makeTransactionIntent(TransactionalEntity user, TransactionalEntity bank, Money money);
	protected abstract ValidationReport validateIntent(TransactionIntent intent);
	protected abstract void reserverParticipantsBalance(TransactionIntent intent);

}
