package com.danabijak.demo.banking.services;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;


import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.danabijak.demo.banking.GlobalMethodsForTesting;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntent;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntentBuilder;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntentStatus;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntentStatus.TRANSFER_STATUS;
import com.danabijak.demo.banking.domain.transactions.exceptions.TransactionIntentPublishException;
import com.danabijak.demo.banking.domain.transactions.repositories.TransactionIntentRepository;
import com.danabijak.demo.banking.domain.transactions.services.DepositIntentService;
import com.danabijak.demo.banking.domain.users.entity.User;

public class DepositIntentServiceTests {
	
	@Mock
	private TransactionIntentRepository transactionIntentRepo;

	@InjectMocks
	private DepositIntentService depositIntentService = new DepositIntentService();
	
	@org.junit.Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	
	@Test
	public void testAttemptPublish_valid_intent_is_saved_to_repo(){
		User beneficiary = GlobalMethodsForTesting.getDummyUserWithBankAccountSetTo(3000);
		User source = GlobalMethodsForTesting.getDummyUserWithBankAccountSetTo(3000);

		TransactionIntent validIntent = new TransactionIntentBuilder()
				.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Deposit"))
				.beneficiary(beneficiary)
				.source(source)
				.amount(Money.of(CurrencyUnit.USD, 123.45))
				.build();
		
		depositIntentService.publish(validIntent);
		
		verify(transactionIntentRepo).save(validIntent); 
		
	}
	
	@Test(expected = TransactionIntentPublishException.class)
	public void testAttemptPublish_invalid_intent_throws(){
		User beneficiary = GlobalMethodsForTesting.getDummyUserWithBankAccountSetTo(30);
		User source = GlobalMethodsForTesting.getDummyUserWithBankAccountSetTo(30);
		
		TransactionIntent invalidIntent = new TransactionIntentBuilder()
				.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Deposit"))
				.beneficiary(beneficiary)
				.source(source)
				.amount(Money.of(CurrencyUnit.USD, 12309133.45))
				.build();
		
		depositIntentService.publish(invalidIntent);		
	}
	
	@Test
	public void testAttemptPublish_beneficiary_depo_limit_decreased(){
		User beneficiary = GlobalMethodsForTesting.getDummyUserWithBankAccountSetTo(100);
		User source = GlobalMethodsForTesting.getDummyUserWithBankAccountSetTo(100);
		Money transactionAmount = Money.of(CurrencyUnit.USD, 200);

		BigDecimal depoLimitBefore = beneficiary.getLimits().getAllowedDeposit();
		TransactionIntent intent = new TransactionIntentBuilder()
				.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Deposit"))
				.beneficiary(beneficiary)
				.source(source)
				.amount(transactionAmount)
				.build();
		
		depositIntentService.publish(intent);	
		BigDecimal depoLimitAfter = intent.beneficiary.getLimits().getAllowedDeposit();
		assertTrue(depoLimitBefore.subtract(transactionAmount.getAmount()).compareTo(depoLimitAfter) == 0);
	}

	

}
