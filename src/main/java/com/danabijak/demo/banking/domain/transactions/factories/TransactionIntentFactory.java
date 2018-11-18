package com.danabijak.demo.banking.domain.transactions.factories;

import java.util.concurrent.CompletableFuture;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.danabijak.demo.banking.domain.users.entity.User;
import com.danabijak.demo.banking.domain.users.services.UserService;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntent;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntentBuilder;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntentStatus;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntentStatus.TRANSFER_STATUS;

@Component
public class TransactionIntentFactory {
	@Autowired
	private UserService userService;

	// TODO: Remove unneeded complexity of CompletableFuture<user> here.
	@Async("asyncExecutor")
	public CompletableFuture<TransactionIntent> createDepositIntent(long beneficiaryId, Money money) {		
		CompletableFuture<User> bank = userService.findByUsername("bankItself@bank.com");
		CompletableFuture<User> user = userService.find(beneficiaryId);
		
		CompletableFuture<Void> allUserFutures = CompletableFuture.allOf(bank, user);
		
		return allUserFutures.thenApply(it -> {
		    User bUser = bank.join();
		    User uUser = user.join();
		    
		    
		    TransactionIntent intent = new TransactionIntentBuilder()
					.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Deposit"))
					.beneficiary(uUser)
					.source(bUser)
					.amount(money)
					.build();
			System.out.println("createDepositIntent() | Intent built: "+intent.toString());

		    return intent;
			
		});
	}
	
	@Async("asyncExecutor")
	public CompletableFuture<TransactionIntent> createWithdrawIntent(long sourceId, Money money) {		
		CompletableFuture<User> bank = userService.findByUsername("bankItself@bank.com");
		CompletableFuture<User> user = userService.find(sourceId);
		
		CompletableFuture<Void> allUserFutures = CompletableFuture.allOf(bank, user);
		
		return allUserFutures.thenApply(it -> {
		    User bUser = bank.join();
		    User uUser = user.join();
		    
		    
		    TransactionIntent intent = new TransactionIntentBuilder()
					.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Deposit"))
					.beneficiary(bUser)
					.source(uUser)
					.amount(money)
					.build();
		    
		    return intent;
			
		});
	}
}
