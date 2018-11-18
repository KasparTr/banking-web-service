package com.danabijak.demo.banking.domain.transactions.services;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.danabijak.demo.banking.GlobalMethodsForTesting;
import com.danabijak.demo.banking.domain.accounts.entity.BankAccount;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntent;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntentBuilder;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntentStatus;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntentStatus.TRANSFER_STATUS;
import com.danabijak.demo.banking.domain.transactions.exceptions.TransactionIntentException;
import com.danabijak.demo.banking.domain.transactions.repositories.TransactionIntentRepository;
import com.danabijak.demo.banking.domain.transactions.services.DepositIntentService;
import com.danabijak.demo.banking.domain.transactions.services.WithdrawIntentService;
import com.danabijak.demo.banking.domain.users.entity.User;

public class WithdrawIntentServiceTests {
	
	@Mock
	private TransactionIntentRepository transactionIntentRepo;

	@InjectMocks
	private WithdrawIntentService withdrawntentService = new WithdrawIntentService();
	
	@org.junit.Before
	public void setUp() throws Exception {
		// Initialize mocks created above
	    MockitoAnnotations.initMocks(this);
	}

	
	// NOTE: Abstract class method 'publish()' of TransactionIntentService
	//			which WithdrawIntentService & DepositIntentService
	//			extend, is tested in DepositIntentServiceTests.
	//			since the superclass method publish() doesn't discriminate on transaction type.
	
	
	@Test
	public void testAttemptPublish_source_withdraw_limit_decreased_by_amount_on_intent(){
		User beneficiary = GlobalMethodsForTesting.getDummyUserWithBankAccountSetTo(1000);
		User source = GlobalMethodsForTesting.getDummyUserWithBankAccountSetTo(1000);
		Money transactionAmount = Money.of(CurrencyUnit.USD, 20);

		BigDecimal withdrawLimitBefore = source.getLimits().getAllowedWithdrawal();
		TransactionIntent intent = new TransactionIntentBuilder()
				.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Deposit"))
				.beneficiary(beneficiary)
				.source(source)
				.amount(transactionAmount)
				.build();
		
		withdrawntentService.publish(intent);	
		BigDecimal withdrawLimitAfter = intent.source.getLimits().getAllowedWithdrawal();
		assertTrue(withdrawLimitBefore.subtract(transactionAmount.getAmount()).compareTo(withdrawLimitAfter) == 0);
	}
}
