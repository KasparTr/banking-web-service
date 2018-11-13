package com.danabijak.demo.banking.transactions.factories;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.concurrent.CompletableFuture;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.danabijak.demo.banking.transactions.entity.TransactionIntent;
import com.danabijak.demo.banking.transactions.entity.TransactionIntentStatus;
import com.danabijak.demo.banking.transactions.entity.TransactionIntentStatus.TRANSFER_STATUS;
import com.danabijak.demo.banking.transactions.http.TransactionIntentClientRequest;
import com.danabijak.demo.banking.transactions.model.TransactionClientRequest;
import com.danabijak.demo.banking.transactions.model.TransactionIntentBuilder;
import com.danabijak.demo.banking.users.entity.User;
import com.danabijak.demo.banking.users.services.UserService;

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
