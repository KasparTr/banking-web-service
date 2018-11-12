package com.danabijak.demo.banking.factories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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

import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.entity.TransactionIntentStatus;
import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.infra.repositories.UserRepository;
import com.danabijak.demo.banking.transactions.factories.TransactionIntentFactory;
import com.danabijak.demo.banking.transactions.http.TransactionIntentClientResponse;
import com.danabijak.demo.banking.users.exceptions.UserObjectNotValidException;
import com.danabijak.demo.banking.users.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionIntentFactoryTests {
	
	// Not using BeforeClass here because static methods don't work with @Autowired
	private static boolean setUpIsDone = false;
	
	
	private static long EXISTING_USER_ID = 2000;
	
	private static String VALID_USERNAME_EXAMPLE = "test@email.com";
	private static String BANK_USERNAME_EXAMPLE = "bankItself@bank.com";

	private static String VALID_PASSWORD_EXAMPLE = "pAS24@a3asd2KSH";

	
	@Mock
	private UserService userService;

	@InjectMocks
	@Resource
	private TransactionIntentFactory transactionIntentFactory;
	
	@org.junit.Before
	public void setUp() throws Exception {
		if(!setUpIsDone) {
			// Initialize mocks created above
		    MockitoAnnotations.initMocks(this);
		    
		    // Change Mocks behavior for user queries
		    User nUser = new User(VALID_USERNAME_EXAMPLE, VALID_PASSWORD_EXAMPLE);
		    CompletableFuture<User> uFuture = new CompletableFuture<User>();
		    uFuture.complete(nUser);
		    when(userService.find(EXISTING_USER_ID)).thenReturn(uFuture);
		    setUpIsDone = true;
		}
	}
	
	@Test
	public void testCreateDepositIntent_returns_correct_intent() {
		Money money = Money.of(CurrencyUnit.USD, 123.12);
		
		CompletableFuture<TransactionIntent> depositIntentFuture = 
				transactionIntentFactory.createDepositIntent(
						EXISTING_USER_ID,
						money);
		depositIntentFuture.thenAccept(intent -> {
			System.out.println("intent: "+intent.toString());
			assertEquals(VALID_USERNAME_EXAMPLE, intent.beneficiary.getName());
			assertEquals(BANK_USERNAME_EXAMPLE, intent.source.getName());
			assertTrue(money.compareTo(intent.amount) == 0);
			assertEquals(TransactionIntentStatus.TRANSFER_STATUS.CREATED, intent.status);
		});
		

	}

}
