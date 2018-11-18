package com.danabijak.demo.banking.domain.transactions.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
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
import com.danabijak.demo.banking.domain.transactions.exceptions.TransactionIntentException;
import com.danabijak.demo.banking.domain.transactions.exceptions.TransactionNotFoundException;
import com.danabijak.demo.banking.domain.transactions.exceptions.TransactionServiceException;
import com.danabijak.demo.banking.domain.transactions.repositories.TransactionRepository;
import com.danabijak.demo.banking.domain.transactions.services.DepositService;
import com.danabijak.demo.banking.domain.transactions.services.TransactionService;
import com.danabijak.demo.banking.domain.transactions.services.TransactionServiceImpl;
import com.danabijak.demo.banking.domain.transactions.valueobjects.TransactionIntentResponse;
import com.danabijak.demo.banking.domain.users.entity.User;

public class TransactionServiceTests {
		
	private final User testSourceUser= GlobalMethodsForTesting.getDummyDefaultUser();
	private final User testBeneficiaryUser= GlobalMethodsForTesting.getDummyDefaultUser();
	private final Money validTransactionAmount = Money.of(CurrencyUnit.USD, 12.34);
	private final Money inValidTransactionAmount = Money.of(CurrencyUnit.USD, 12241221.34);
	private final long TEST_TRANSACTION_ID = 1;
	private final long WRONG_TRANSACTION_ID = -1;

	
	@Mock
	private TransactionRepository transactionRepo;
	
	@Mock
	private AccountRepository accountRepository;
	
	@InjectMocks
	private TransactionService transactionService = new DepositService();
	
	@org.junit.Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		Transaction testTransaction = new Transaction(
				validTransactionAmount, 
				testBeneficiaryUser.getBankAccount(), 
				testSourceUser.getBankAccount(), 
				"Test transaction");
		
		testTransaction.setId(TEST_TRANSACTION_ID);
		
		when(accountRepository.findById(testBeneficiaryUser.getBankAccount().getId())).thenReturn(
				Optional.of(testBeneficiaryUser.getBankAccount()));
		
		when(accountRepository.findById(testSourceUser.getBankAccount().getId())).thenReturn(
				Optional.of(testSourceUser.getBankAccount()));	
		
		when(transactionRepo.findById(TEST_TRANSACTION_ID)).thenReturn(
				Optional.of(testTransaction));
	}
	
	@Test(expected = TransactionServiceException.class)
	public void testProcess_invalid_intent_process_throws() {
		TransactionIntent invalidIntent = new TransactionIntentBuilder()
				.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Deposit"))
				.beneficiary(testBeneficiaryUser)
				.source(testSourceUser)
				.amount(inValidTransactionAmount)
				.build();
		
		invalidIntent.setIntentAsNotValid();
		transactionService.process(invalidIntent);
	}
	
	@Test
	public void testProcess_invalid_intent_process_doesnt_invoke_transaction_repo_save(){
		TransactionIntent invalidIntent = new TransactionIntentBuilder()
				.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Deposit"))
				.beneficiary(testBeneficiaryUser)
				.source(testSourceUser)
				.amount(inValidTransactionAmount)
				.build();
		
		invalidIntent.setIntentAsNotValid();
		try {
			transactionService.process(invalidIntent);
		}catch(TransactionServiceException e) {
			verify(transactionRepo, never()).save(Mockito.any(Transaction.class)); 
		}
	}
	
	@Test
	public void testProcess_create_valid_transaction_from_intent() {
		TransactionIntent intent = new TransactionIntentBuilder()
				.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Deposit"))
				.beneficiary(testBeneficiaryUser)
				.source(testSourceUser)
				.amount(validTransactionAmount)
				.build();
		
		intent.setIntentAsValid();
				
		CompletableFuture<Transaction> transactionFuture = transactionService.process(intent);
		try {
			Transaction transaction = transactionFuture.get();
			System.out.println("Transaction: " + transaction.toString());
			assertTrue(intent.amount.compareTo(transaction.amount) == 0);
			assertEquals(intent.beneficiary.getBankAccount().getId(), transaction.beneficiaryAccount.getId());
			assertEquals(intent.source.getBankAccount().getId(), transaction.sourceAccount.getId());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testProcess_save_transaction_to_repository() {
		TransactionIntent intent = new TransactionIntentBuilder()
				.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Deposit"))
				.beneficiary(testBeneficiaryUser)
				.source(testSourceUser)
				.amount(validTransactionAmount)
				.build();
		
		intent.setIntentAsValid();
		
		CompletableFuture<Transaction> transactionFuture = transactionService.process(intent);
		try {
			transactionFuture.get();
			verify(transactionRepo).save(Mockito.any(Transaction.class));
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testFindTransaction_return_correct_transaction() {
		CompletableFuture<Transaction> transactionFuture = transactionService.findTransactionBy(TEST_TRANSACTION_ID);
		try {
			Transaction transaction = transactionFuture.get();
			assertEquals(TEST_TRANSACTION_ID, transaction.id);
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test(expected = TransactionNotFoundException.class)
	public void testFindTransaction_throw_on_wrong_transaction_id() throws Throwable {
		CompletableFuture<Transaction> transactionFuture = transactionService.findTransactionBy(WRONG_TRANSACTION_ID);
		try {
			transactionFuture.get();
		} catch (Exception e) {
			throw e.getCause();
		}
	}
}
