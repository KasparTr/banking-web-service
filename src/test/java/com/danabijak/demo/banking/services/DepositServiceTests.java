package com.danabijak.demo.banking.services;

import static org.junit.Assert.assertEquals;
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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.danabijak.demo.banking.GlobalMethodsForTesting;
import com.danabijak.demo.banking.domain.accounts.entity.BankAccount;
import com.danabijak.demo.banking.domain.accounts.repositories.AccountRepository;
import com.danabijak.demo.banking.domain.transactions.entity.Transaction;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntent;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntentBuilder;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntentStatus;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntentStatus.TRANSFER_STATUS;
import com.danabijak.demo.banking.domain.transactions.exceptions.TransactionServiceException;
import com.danabijak.demo.banking.domain.transactions.repositories.TransactionRepository;
import com.danabijak.demo.banking.domain.transactions.services.DepositService;
import com.danabijak.demo.banking.domain.transactions.services.TransactionService;
import com.danabijak.demo.banking.domain.transactions.services.TransactionServiceImpl;
import com.danabijak.demo.banking.domain.transactions.valueobjects.TransactionIntentResponse;
import com.danabijak.demo.banking.domain.users.entity.User;

public class DepositServiceTests {
		
	private static String VALID_USERNAME_EXAMPLE = "test@email.com";
	private static String VALID_PASSWORD_EXAMPLE = "pAS24@a3asd2KSH";
	
	@Mock
	private TransactionRepository transactionRepo;
	
	@Mock
	private AccountRepository accountRepository;
	
	@InjectMocks
	private TransactionService depositService = new DepositService();
	
	@org.junit.Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		
		BankAccount ba = GlobalMethodsForTesting.getDummyDefaultUser().getBankAccount();
		Optional<BankAccount> account = Optional.of(ba);
		ba.setBalance(new BigDecimal(3423423));
		when(accountRepository.findById(12l)).thenReturn(account);
	}
	
	@Test(expected = TransactionServiceException.class)
	public void testProcess_dont_process_invalid_intent_but_throw() {
		Money money = Money.of(CurrencyUnit.USD, 123.12);
		TransactionIntent intent = new TransactionIntentBuilder()
				.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Deposit"))
				.beneficiary(GlobalMethodsForTesting.getDummyDefaultUser())
				.source(GlobalMethodsForTesting.getDummyDefaultUser())
				.amount(money)
				.build();
		
		intent.setIntentAsNotValid();
		depositService.process(intent);
		
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
		
		CompletableFuture<Transaction> transFuture = depositService.process(intent);
		
		
		System.out.println("Future : " + transFuture);

		transFuture.thenApply(transaction -> {
			System.out.println("Transaction: " + transaction.toString());
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
		
		depositService.process(intent).thenApply(transaction -> {
			verify(transactionRepo).save(Mockito.any(Transaction.class));
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
		
		depositService.process(intent).thenApply(transaction -> {
			assertTrue(startBalance.plus(money).isEqual(endBalance));
			return null;	//to resolve the thenApply
		});
	}
}
