package com.danabijak.demo.banking.domain.accounts.services;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
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
import com.danabijak.demo.banking.domain.transactions.entity.Transaction;
import com.danabijak.demo.banking.domain.transactions.repositories.TransactionRepository;
import com.danabijak.demo.banking.domain.users.factories.BankAccountStatementFactory;

public class AccountServiceTests {
			
	private static String QUERY_ACCOUNT_NAME = "test@email.com";
	private static String OTHER_ACCOUNT_NAME = "test@email.com";
	private static long BANK_ACCOUNT_ID_NON_EXISTING= -124512;
	private static long BANK_ACCOUNT_ID_EXISTING= 12;

	
	private BankAccount testAccountToQueryWith = new BankAccount(CurrencyUnit.USD, QUERY_ACCOUNT_NAME);
	private BankAccount otherAccount = new BankAccount(CurrencyUnit.USD, OTHER_ACCOUNT_NAME);
	private Money money = Money.of(CurrencyUnit.USD, new BigDecimal(344.00));

	@Mock
	private TransactionRepository transactionRepo;
	@Mock
	private AccountRepository accountRepository;
	@Mock
	private BankAccountStatementFactory baStatementFactory;
	
	@InjectMocks
	AccountServiceImpl accountService = new AccountServiceImpl();

	@org.junit.Before
	public void setUp() throws Exception {
		// Initialize mocks created above
	    MockitoAnnotations.initMocks(this);
	    
	    // Change Mocks behavior for user queries
	    List<Transaction> allKindsOfTransactions = new ArrayList<>();
	    allKindsOfTransactions.add(new Transaction(money, testAccountToQueryWith, otherAccount, "Test"));
	    allKindsOfTransactions.add(new Transaction(money, testAccountToQueryWith, otherAccount, "Test"));
	    allKindsOfTransactions.add(new Transaction(money, otherAccount, testAccountToQueryWith, "Test"));
	    allKindsOfTransactions.add(new Transaction(money, otherAccount, testAccountToQueryWith, "Test"));
	    when(transactionRepo.findAll()).thenReturn(allKindsOfTransactions);
	    
	    BankAccount existingBankAccount = new BankAccount(CurrencyUnit.USD, OTHER_ACCOUNT_NAME);
	    when(accountRepository.findById(BANK_ACCOUNT_ID_EXISTING)).thenReturn(Optional.of(existingBankAccount));
	}
	
	@Test
	public void testGetDebitTransactionsOf_all_transactions_are_debit() {
		accountService.getDebitTransactionsOf(testAccountToQueryWith).thenApply(transactions ->{
			for(Transaction t:transactions) {
				if(t.sourceAccount.getId() != testAccountToQueryWith.getId()) fail(
						"Transaction found where source (benefactor) account was not the account whose debit transactions were queried");
			}
			return null;
		});
	}
	
	@Test
	public void testGetCreditTransactionsOf_all_transactions_are_credit() {
		accountService.getCreditTransactionsOf(testAccountToQueryWith).thenApply(transactions ->{
			for(Transaction t:transactions) {
				if(t.beneficiaryAccount.getId() != testAccountToQueryWith.getId()) {
					System.out.println("AccountServiceTest | testGetDebitTransactionsOf_all_transactions_are_credit | credit trans found!");
					fail("Transaction found where beneficiary account was not the account whose credit transactions were queried");

				}
			}
			return null;
		});
	}

	
	@Test(expected = BankAccountException.class)
	public void testGetBankAccount_thow_if_no_bank_account_found() {
		accountService.getBankAccount(BANK_ACCOUNT_ID_NON_EXISTING);
	}

	@Test
	public void testGetBankAccount_return_correct_account() {
		accountService.getBankAccount(BANK_ACCOUNT_ID_EXISTING).thenApply(account ->{
			if(account.getId() != BANK_ACCOUNT_ID_EXISTING)fail();
			return null;
		});
	}
	
	
	
}
