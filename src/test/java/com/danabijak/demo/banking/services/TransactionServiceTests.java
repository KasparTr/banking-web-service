package com.danabijak.demo.banking.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Resource;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.danabijak.demo.banking.GlobalMethodsForTesting;
import com.danabijak.demo.banking.entity.Transaction;
import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.entity.TransactionIntentStatus;
import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.entity.TransactionIntentStatus.TRANSFER_STATUS;
import com.danabijak.demo.banking.infra.repositories.TransactionRepository;
import com.danabijak.demo.banking.transactions.exceptions.TransactionServiceException;
import com.danabijak.demo.banking.transactions.http.TransactionIntentClientResponse;
import com.danabijak.demo.banking.transactions.model.TransactionIntentBuilder;
import com.danabijak.demo.banking.transactions.services.TransactionServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionServiceTests {
	
	// Not using BeforeClass here because static methods don't work with @Autowired
	private static boolean setUpIsDone = false;
		
	private static String VALID_USERNAME_EXAMPLE = "test@email.com";
	private static String VALID_PASSWORD_EXAMPLE = "pAS24@a3asd2KSH";
	
	@Mock
	private TransactionRepository transactionRepo;
	
	@InjectMocks
	@Resource
	private TransactionServiceImpl transactionService;
	
	@org.junit.Before
	public void setUp() throws Exception {
		if(!setUpIsDone) {
			// Initialize mocks created above
		    MockitoAnnotations.initMocks(this);
		    setUpIsDone = true;
		}
	}
	
	@Test(expected = TransactionServiceException.class)
	public void testProcess_dont_process_invalid_intent() {
		Money money = Money.of(CurrencyUnit.USD, 123.12);
		TransactionIntent intent = new TransactionIntentBuilder()
				.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Deposit"))
				.beneficiary(GlobalMethodsForTesting.getDummyDefaultUser())
				.source(GlobalMethodsForTesting.getDummyDefaultUser())
				.amount(money)
				.build();
		
		intent.setIntentAsNotValid();
		transactionService.porcess(intent);
		
	}
	
	@Test
	public void testProcess_create_valid_transaction_from_intent() {
		Money money = Money.of(CurrencyUnit.USD, 23.12);
		TransactionIntent intent = new TransactionIntentBuilder()
				.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Deposit"))
				.beneficiary(GlobalMethodsForTesting.getDummyDefaultUser())
				.source(GlobalMethodsForTesting.getDummyDefaultUser())
				.amount(money)
				.build();
		
		intent.setIntentAsValid();
		
		transactionService.porcess(intent).thenApply(transaction -> {
			assertTrue(intent.amount.compareTo(transaction.amount) == 0);
			assertEquals(intent.beneficiary.getBankAccount().getId(), transaction.beneficiaryAccount.getId());
			assertEquals(intent.source.getBankAccount().getId(), transaction.sourceAccount.getId());
			return null;	//to resolve the thenApply
		});
		
		
		
	}
	
	@Test
	public void testProcess_save_transaction_to_repository() {
		Money money = Money.of(CurrencyUnit.USD, 23.12);
		TransactionIntent intent = new TransactionIntentBuilder()
				.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Deposit"))
				.beneficiary(GlobalMethodsForTesting.getDummyDefaultUser())
				.source(GlobalMethodsForTesting.getDummyDefaultUser())
				.amount(money)
				.build();
		
		intent.setIntentAsValid();
		
		transactionService.porcess(intent).thenApply(transaction -> {
			verify(transactionRepo).save(Mockito.any(Transaction.class));
			return null;	//to resolve the thenApply
		});
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

		transactionService.porcess(intent).thenApply(transaction -> {
			assertTrue(startBalance.minus(money).isEqual(endBalance));
			return null;	//to resolve the thenApply
		});

		
	}
	
	@Test
	public void testProcess_beneficiary_balance_increased_by_transaction_amount() {
		Money money = Money.of(CurrencyUnit.USD, 25.00);
		User beneficiaryUser = GlobalMethodsForTesting.getDummyDefaultUser();
		Money startBalance = beneficiaryUser.getBankAccount().getBalance();
		TransactionIntent intent = new TransactionIntentBuilder()
				.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Deposit"))
				.beneficiary(beneficiaryUser )
				.source(GlobalMethodsForTesting.getDummyDefaultUser())
				.amount(money)
				.build();
		
		intent.setIntentAsValid();
		Money endBalance = beneficiaryUser.getBankAccount().getBalance();
		
		transactionService.porcess(intent).thenApply(transaction -> {
			assertTrue(startBalance.plus(money).isEqual(endBalance));
			return null;	//to resolve the thenApply
		});
	}
}
