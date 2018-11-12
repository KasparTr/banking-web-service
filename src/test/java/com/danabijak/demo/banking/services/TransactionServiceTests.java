package com.danabijak.demo.banking.services;

import static org.junit.Assert.assertEquals;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.danabijak.demo.banking.entity.Balance;
import com.danabijak.demo.banking.entity.BankAccount;
import com.danabijak.demo.banking.entity.Transaction;
import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.entity.TransactionIntentStatus;
import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.entity.TransactionIntentStatus.TRANSFER_STATUS;
import com.danabijak.demo.banking.infra.repositories.TransactionRepository;
import com.danabijak.demo.banking.transactions.exceptions.TransactionServiceException;
import com.danabijak.demo.banking.transactions.factories.TransactionIntentFactory;
import com.danabijak.demo.banking.transactions.model.TransactionIntentBuilder;
import com.danabijak.demo.banking.transactions.services.TransactionServiceImpl;
import com.danabijak.demo.banking.users.exceptions.UserNotFoundException;
import com.danabijak.demo.banking.users.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionServiceTests {
	
private static long EXISTING_USER_ID = 2000;
	
	private static long VALID_UID_EXAMPLE = 13;
	private static String VALID_USERNAME_EXAMPLE = "test@email.com";
	private static String VALID_PASSWORD_EXAMPLE = "pAS24@a3asd2KSH";
	
	@Mock
	private TransactionRepository transactionRepo;
	
	@InjectMocks
	@Resource
	private TransactionServiceImpl transactionService;
	
	@Test(expected = TransactionServiceException.class)
	public void testProcess_dont_process_invalid_intent() {
		Money money = Money.of(CurrencyUnit.USD, 123.12);
		TransactionIntent intent = new TransactionIntentBuilder()
				.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Deposit"))
				.beneficiary(getDummyUser())
				.source(getDummyUser())
				.amount(money)
				.build();
		
		intent.setIntentAsNotValid();
		transactionService.porcess(intent);
		
	}
	
	@Test
	public void testProcess_create_valid_transaction_from_intent() {
		Money money = Money.of(CurrencyUnit.USD, 123.12);
		TransactionIntent intent = new TransactionIntentBuilder()
				.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Deposit"))
				.beneficiary(getDummyUser())
				.source(getDummyUser())
				.amount(money)
				.build();
		
		intent.setIntentAsValid();
		Transaction transaction = transactionService.porcess(intent);
		
		assertTrue(intent.amount.compareTo(transaction.amount) == 0);
		assertEquals(intent.beneficiary.getBankAccount().getId(), transaction.beneficiaryAccount.getId());
		assertEquals(intent.source.getBankAccount().getId(), transaction.sourceAccount.getId());
	}
	
	@Test
	public void testProcess_save_transaction_to_repository() {
		Money money = Money.of(CurrencyUnit.USD, 123.12);
		TransactionIntent intent = new TransactionIntentBuilder()
				.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Deposit"))
				.beneficiary(getDummyUser())
				.source(getDummyUser())
				.amount(money)
				.build();
		
		intent.setIntentAsValid();
		Transaction transaction = transactionService.porcess(intent);
		verify(transactionRepo).save(transaction);
	}

	private User getDummyUser() {
		User user = new User(VALID_USERNAME_EXAMPLE,VALID_PASSWORD_EXAMPLE);

		BankAccount ba = new BankAccount(BankAccount.DEFAULT_CURRENCY.USD, user.getUsername());
		ba.getBalance().setTotalAmount(Balance.DEFAULT_LIMITS.BANKING_USER_START_BALANCE);
		user.attachBankAccount(ba);

		return user;
	}
}
