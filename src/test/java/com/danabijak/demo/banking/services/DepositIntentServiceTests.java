package com.danabijak.demo.banking.services;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Resource;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import com.danabijak.demo.banking.transactions.factories.TransactionIntentFactory;
import com.danabijak.demo.banking.transactions.model.TransactionIntentBuilder;
import com.danabijak.demo.banking.transactions.services.DepositIntentService;
import com.danabijak.demo.banking.transactions.services.TransactionIntentService;
import com.danabijak.demo.banking.transactions.services.TransactionIntentServiceImpl;
import com.danabijak.demo.banking.users.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DepositIntentServiceTests {
	
	// Not using BeforeClass here because static methods don't work with @Autowired
	private static boolean setUpIsDone = false;
	
	
	
	@Mock
	private TransactionIntentRepository transactionIntentRepo;

	@InjectMocks
	@Resource
	private DepositIntentService depositIntentService;
	
	@org.junit.Before
	public void setUp() throws Exception {
		if(!setUpIsDone) {
			// Initialize mocks created above
		    MockitoAnnotations.initMocks(this);
		    setUpIsDone = true;
		}
	    
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
