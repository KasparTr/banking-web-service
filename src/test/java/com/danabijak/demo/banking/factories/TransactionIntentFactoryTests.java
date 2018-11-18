package com.danabijak.demo.banking.factories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.danabijak.demo.banking.GlobalMethodsForTesting;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntent;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntentStatus;
import com.danabijak.demo.banking.domain.transactions.factories.TransactionIntentFactory;
import com.danabijak.demo.banking.domain.transactions.valueobjects.TransactionIntentResponse;
import com.danabijak.demo.banking.domain.users.entity.User;
import com.danabijak.demo.banking.domain.users.exceptions.UserObjectNotValidException;
import com.danabijak.demo.banking.domain.users.repositories.UserRepository;
import com.danabijak.demo.banking.domain.users.services.UserService;


public class TransactionIntentFactoryTests {
	
	// Not using BeforeClass here because static methods don't work with @Autowired
	private static boolean setUpIsDone = false;
	
	
	private static long EXISTING_USER_ID = 2000;
	
	@Mock
	private UserService userService;

	@InjectMocks
	private TransactionIntentFactory transactionIntentFactory = new TransactionIntentFactory();
	
	@org.junit.Before
	public void setUp() throws Exception {
		// Initialize mocks created above
	    MockitoAnnotations.initMocks(this);
	    
	    // Change Mocks behavior for user queries
	    User nUser = GlobalMethodsForTesting.getDummyDefaultUser();
	    when(userService.find(EXISTING_USER_ID)).thenReturn(CompletableFuture.completedFuture(nUser));
	    
	    User bank = GlobalMethodsForTesting.getDummyBankUser();
	    when(userService.findByUsername("bankItself@bank.com")).thenReturn(CompletableFuture.completedFuture(bank));
	    
	}
	
	@Test
	public void testCreateDepositIntent_returns_correct_intent() {
		Money money = Money.of(CurrencyUnit.USD, 123.12);

		
		CompletableFuture<TransactionIntent> depositIntentFuture = 
				transactionIntentFactory.createDepositIntent(
						EXISTING_USER_ID,
						money);
		try {
			TransactionIntent intent = depositIntentFuture.get();
			System.out.println("intent: "+intent.toString());
			assertEquals(GlobalMethodsForTesting.VALID_USERNAME_EXAMPLE, intent.beneficiary.getName());
			assertEquals(GlobalMethodsForTesting.BANK_USERNAME_EXAMPLE, intent.source.getName());
			assertTrue(money.compareTo(intent.amount) == 0);
			assertEquals(TransactionIntentStatus.TRANSFER_STATUS.CREATED, intent.status.status);
		} catch (Exception e) {
			fail();
		}

	}
	
	@Test
	public void testCreateWithdrawIntent_returns_correct_intent() {
		Money money = Money.of(CurrencyUnit.USD, 23.12);

		
		CompletableFuture<TransactionIntent> depositIntentFuture = 
				transactionIntentFactory.createWithdrawIntent(
						EXISTING_USER_ID,
						money);
		try {
			TransactionIntent intent = depositIntentFuture.get();
			System.out.println("intent: "+intent.toString());
			assertEquals(GlobalMethodsForTesting.VALID_USERNAME_EXAMPLE, intent.source.getName());
			assertEquals(GlobalMethodsForTesting.BANK_USERNAME_EXAMPLE, intent.beneficiary.getName());
			assertTrue(money.compareTo(intent.amount) == 0);
			assertEquals(TransactionIntentStatus.TRANSFER_STATUS.CREATED, intent.status.status);
		} catch (Exception e) {
			fail();
		}

	}

}
