package com.danabijak.demo.banking.services;

import static org.junit.Assert.assertTrue;
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
import com.danabijak.demo.banking.entity.BankAccount;
import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.entity.TransactionIntentStatus;
import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.entity.TransactionIntentStatus.TRANSFER_STATUS;
import com.danabijak.demo.banking.infra.repositories.TransactionIntentRepository;
import com.danabijak.demo.banking.transactions.exceptions.TransactionIntentPublishException;
import com.danabijak.demo.banking.transactions.model.TransactionIntentBuilder;
import com.danabijak.demo.banking.transactions.services.DepositIntentService;
import com.danabijak.demo.banking.transactions.services.WithdrawIntentService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WithdrawIntentServiceTests {
	// Not using BeforeClass here because static methods don't work with @Autowired
	private static boolean setUpIsDone = false;
	
	@Mock
	private TransactionIntentRepository transactionIntentRepo;

	@InjectMocks
	@Resource
	private WithdrawIntentService withdrawntentService;
	
	@org.junit.Before
	public void setUp() throws Exception {
		if(!setUpIsDone) {
			// Initialize mocks created above
		    MockitoAnnotations.initMocks(this);
		    setUpIsDone = true;
		}
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
		
		withdrawntentService.attemptPublish(invalidIntent);		
	}
	
	@Test
	public void testAttemptPublish_source_withdraw_limit_decreased(){
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
		
		withdrawntentService.attemptPublish(intent);	
		BigDecimal withdrawLimitAfter = intent.source.getLimits().getAllowedWithdrawal();
		System.out.println("Withdraw limit before: " + withdrawLimitBefore);
		System.out.println("transactionAmount: " + transactionAmount.getAmount());
		System.out.println("Withdraw limit after: " + withdrawLimitAfter);
		assertTrue(withdrawLimitBefore.subtract(transactionAmount.getAmount()).compareTo(withdrawLimitAfter) == 0);
	}
}
