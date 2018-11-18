package com.danabijak.demo.banking.domain.accounts.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.danabijak.demo.banking.domain.accounts.entity.BankAccount;
import com.danabijak.demo.banking.domain.accounts.exceptions.BankAccountException;
import com.danabijak.demo.banking.domain.accounts.repositories.AccountRepository;
import com.danabijak.demo.banking.domain.accounts.services.AccountServiceImpl;
import com.danabijak.demo.banking.domain.accounts.valueobjects.AccountStatementResponse;
import com.danabijak.demo.banking.domain.transactions.entity.Transaction;
import com.danabijak.demo.banking.domain.transactions.repositories.TransactionRepository;
import com.danabijak.demo.banking.domain.users.factories.BankAccountStatementFactory;

public class AccountServiceTests {
			
	private static String TEST_ACCOUNT_NAME = "test@email.com";
	private static String OTHER_ACCOUNT_NAME = "other@email.com";
	private static long BANK_ACCOUNT_ID_NON_EXISTING= -124512;
	private static long TEST_ACCOUNT_ID= 12;
	private static long OTHER_ACCOUNT_ID= 3;
	private static int TEST_ACCOUNT_NR_OF_TRANSACTIONS= 4;
	private static BigDecimal TEST_ACCOUNT_BALANCE = new BigDecimal(344.00);

	private BankAccount testAccount = new BankAccount(CurrencyUnit.USD, TEST_ACCOUNT_NAME);
	private BankAccount otherAccount = new BankAccount(CurrencyUnit.USD, OTHER_ACCOUNT_NAME);

	@Mock
	private TransactionRepository transactionRepo;
	@Mock
	private AccountRepository accountRepository;
	
	@InjectMocks
	AccountServiceImpl accountService = new AccountServiceImpl();

	@org.junit.Before
	public void setUp() throws Exception {
		testAccount.setId(TEST_ACCOUNT_ID);
		otherAccount.setId(OTHER_ACCOUNT_ID);

		// Initialize mocks created above
	    MockitoAnnotations.initMocks(this);
	    
	    // Change Mocks behavior for user queries
	    List<Transaction> allKindsOfTransactions = new ArrayList<>();
	    Money money = Money.of(CurrencyUnit.USD, TEST_ACCOUNT_BALANCE);
	    for (int i = 0; i < TEST_ACCOUNT_NR_OF_TRANSACTIONS/2; i++) {
	        allKindsOfTransactions.add(new Transaction(money, testAccount, otherAccount, "Test"));
		    allKindsOfTransactions.add(new Transaction(money, otherAccount, testAccount, "Test2"));
	    }
	    when(transactionRepo.findAll()).thenReturn(allKindsOfTransactions);	    
	    testAccount.setBalance(TEST_ACCOUNT_BALANCE);
	    when(accountRepository.findById(TEST_ACCOUNT_ID)).thenReturn(Optional.of(testAccount));
	}
	
	@Test
	public void testGetDebitTransactionsOf_all_transactions_are_debit() {
		CompletableFuture<List<Transaction>> transactionsFuture = accountService.getDebitTransactionsOf(testAccount);
		List<Transaction> transactions;
		try {
			transactions = transactionsFuture.get();
			for(Transaction t:transactions) {
				assertEquals(testAccount.getId(), t.sourceAccount.getId());
			}
		} catch (Exception e) {
			fail();
		}
		
	}
	
	@Test
	public void testGetCreditTransactionsOf_all_transactions_are_credit() {
		CompletableFuture<List<Transaction>> transactionsFuture = accountService.getCreditTransactionsOf(testAccount);
		List<Transaction> transactions;
		try {
			transactions = transactionsFuture.get();
			for(Transaction t:transactions) {
				assertEquals(testAccount.getId(), t.beneficiaryAccount.getId());
			}
		} catch (Exception e) {
			fail();
		}
		
	}

	
	@Test(expected = BankAccountException.class)
	public void testGetBankAccount_thow_if_no_bank_account_found() throws Throwable{
		CompletableFuture<BankAccount> accountFuture = accountService.getBankAccount(BANK_ACCOUNT_ID_NON_EXISTING);
		try {
			accountFuture.get();
		} catch (Exception e) {
			throw e.getCause();
		}
	}

	@Test
	public void testGetBankAccount_return_correct_account() {
		CompletableFuture<BankAccount> accountFuture = accountService.getBankAccount(TEST_ACCOUNT_ID);
		BankAccount account;
		try {
			account = accountFuture.get();
			assertEquals(TEST_ACCOUNT_ID, account.getId());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testGetAccountStatement_bank_account_id_on_statement_matches_request() {
		CompletableFuture<AccountStatementResponse> statementFuture = accountService.getAccountStatement(TEST_ACCOUNT_ID);
		AccountStatementResponse statement;
		try {
			statement = statementFuture.get();
			assertEquals(TEST_ACCOUNT_ID, statement.bankAccountId);
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testGetAccountStatement_bank_account_balance_on_statement_matches_request() {
		CompletableFuture<AccountStatementResponse> statementFuture = accountService.getAccountStatement(TEST_ACCOUNT_ID);
		AccountStatementResponse statement;
		try {
			statement = statementFuture.get();
			assertEquals(TEST_ACCOUNT_BALANCE.compareTo(statement.totalBalance), 0);
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testGetAccountStatement_nr_of_transactions_on_statement_matches_request() {
		CompletableFuture<AccountStatementResponse> statementFuture = accountService.getAccountStatement(TEST_ACCOUNT_ID);
		AccountStatementResponse statement;
		try {
			statement = statementFuture.get();
			assertEquals(TEST_ACCOUNT_NR_OF_TRANSACTIONS, statement.transactions.size());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test(expected = BankAccountException.class)
	public void testGetAccountStatement_thow_if_no_bank_account() {
		CompletableFuture<AccountStatementResponse> statementFuture = accountService.getAccountStatement(BANK_ACCOUNT_ID_NON_EXISTING);
		try {
			statementFuture.get();
		} catch (Exception e) {
			fail();
		}
		
	}
	
	
	
}
