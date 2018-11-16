package com.danabijak.demo.banking.services;

import static org.junit.Assert.assertTrue;

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
import com.danabijak.demo.banking.domain.transactions.repositories.TransactionRepository;
import com.danabijak.demo.banking.domain.transactions.services.DepositService;
import com.danabijak.demo.banking.domain.transactions.services.TransactionServiceImpl;
import com.danabijak.demo.banking.domain.transactions.services.WithdrawService;
import com.danabijak.demo.banking.domain.users.entity.User;

public class WithdrawServiceTests {
	
	
	private static String VALID_USERNAME_EXAMPLE = "test@email.com";
	private static String VALID_PASSWORD_EXAMPLE = "pAS24@a3asd2KSH";
	
	@Mock
	private TransactionRepository transactionRepo;
	
	@InjectMocks
	private TransactionServiceImpl withdrawService = new WithdrawService();
	
	@org.junit.Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testProcess_source_balance_lowered_by_transaction_amount() {
		Money money = Money.of(CurrencyUnit.USD, 25.00);
		User sourceUser = GlobalMethodsForTesting.getDummyDefaultUser();
		Money startBalance = sourceUser.getBankAccount().getBalance();
		TransactionIntent intent = new TransactionIntentBuilder()
				.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Deposit"))
				.beneficiary(GlobalMethodsForTesting.getDummyDefaultUser())
				.source(sourceUser)
				.amount(money)
				.build();
		
		intent.setIntentAsValid();
		Money endBalance = sourceUser.getBankAccount().getBalance();

		withdrawService.process(intent).thenApply(transaction -> {
			assertTrue(startBalance.minus(money).isEqual(endBalance));
			return null;	//to resolve the thenApply
		});

		
	}

}
