package com.danabijak.demo.banking.transactions.services;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.entity.TransactionIntentStatus;
import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.entity.TransactionIntentStatus.TRANSFER_STATUS;
import com.danabijak.demo.banking.infra.repositories.TransactionIntentRepository;
import com.danabijak.demo.banking.transactions.exceptions.TransactionIntentPublishException;
import com.danabijak.demo.banking.transactions.model.TransactionClientRequest;
import com.danabijak.demo.banking.transactions.model.TransactionIntentBuilder;
import com.danabijak.demo.banking.transactions.model.ValidationReport;
import com.danabijak.demo.banking.users.services.UserService;
import com.danabijak.demo.banking.validators.DepositIntentValidator;
import com.danabijak.demo.banking.validators.TransactionIntentValidator;

@Service
//@Resource(name="depositIntentService")
//public class DepositIntentService extends TransactionIntentServiceImpl{
public class DepositIntentService implements TransactionIntentService{
	
	@Autowired
	private UserService userService;

	
	@Autowired
	private TransactionIntentRepository transactionIntentRepo;
	
	// This faile because the User (or TransactionalEntity) is attached in another thread than the TransactionIntent is created.
	@Override
	@Async("asyncExecutor")
	public CompletableFuture<TransactionIntent> publish(TransactionClientRequest request) throws TransactionIntentPublishException {
		
		CompletableFuture<User> bank = userService.findByUsername("bankItself@bank.com");
		CompletableFuture<User> user = userService.find(request.entity.id);
		Money money = Money.of(CurrencyUnit.of(request.money.currency), request.money.amount);
		
		CompletableFuture<Void> allUserFutures = CompletableFuture.allOf(bank, user);
		
		return allUserFutures.thenApply(it -> {
		    User userSource = bank.join();
		    User userBeneficiary = user.join();
		    
		    
		    TransactionIntent intent = new TransactionIntentBuilder()
					.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Deposit"))
					.beneficiary(userBeneficiary)
					.source(userSource)
					.amount(money)
					.build();
		    
		    ValidationReport validationReport = validateIntent(intent);
			
			if(validationReport.valid) {
				System.out.println("TransactionIntentServiceImpl | attemptPublish() | intent validated: " + intent.toString());
				transactionIntentRepo.save(intent);
				System.out.println("TransactionIntentServiceImpl | attemptPublish() | intent saved");

				reserverParticipantsBalance(intent);
				return publish(intent);
			}else {
				throw new TransactionIntentPublishException("Transaction intent not publised. Errors: " + validationReport.generateStringMessage());
			}
			
		});
	}
	
	@Transactional
	public TransactionIntent attemptPublish(TransactionIntent intent){
		System.out.println("TransactionIntentServiceImpl | attemptPublish() | transactionIntentRepo: " + transactionIntentRepo);

		ValidationReport validationReport = validateIntent(intent);
		
		if(validationReport.valid) {
			System.out.println("TransactionIntentServiceImpl | attemptPublish() | intent validated: " + intent.toString());
			transactionIntentRepo.save(intent);
			System.out.println("TransactionIntentServiceImpl | attemptPublish() | intent saved");

			reserverParticipantsBalance(intent);
			return publish(intent);
		}else {
			throw new TransactionIntentPublishException("Transaction intent not publised. Errors: " + validationReport.generateStringMessage());
		}
	}
	

	/**
	 * NB! Publishing intents to messaging channel is not implement yet.
	 * TODO: Instead of directly sending intent to TransactionSerice for processing, publish the intent to the intent pool and 
	 * 	have the TransactionService (as a subscriber) process the intents.
	 */
	protected TransactionIntent publish(TransactionIntent intent) {
		// TODO: Implement publishing to PUB/SUB channel. 
		// NB! if that fails keep in mind to undo reserver balance/limits!

		return intent;
	}

	//@Override
	protected ValidationReport validateIntent(TransactionIntent intent) {
		TransactionIntentValidator validator = new DepositIntentValidator();
		return validator.validate(intent);
	}
	
	//@Override
	protected void reserverParticipantsBalance(TransactionIntent intent) {
		intent.beneficiary.getLimits().decreaseAllowedDeposit(intent.amount.getAmount());
	}


	
}
