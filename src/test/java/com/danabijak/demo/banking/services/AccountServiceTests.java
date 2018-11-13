package com.danabijak.demo.banking.services;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.danabijak.demo.banking.accounts.entity.BankAccount;
import com.danabijak.demo.banking.accounts.services.AccountService;
import com.danabijak.demo.banking.accounts.services.AccountServiceImpl;
import com.danabijak.demo.banking.infra.repositories.TransactionRepository;
import com.danabijak.demo.banking.transactions.entity.Transaction;
import com.danabijak.demo.banking.transactions.services.TransactionServiceImpl;
import com.danabijak.demo.banking.users.entity.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTests {
	
	// Not using BeforeClass here because static methods don't work with @Autowired
	private static boolean setUpIsDone = false;
		
	private static String QUERY_ACCOUNT_NAME = "test@email.com";
	private static String OTHER_ACCOUNT_NAME = "test@email.com";
	
	private BankAccount testAccountToQueryWith = new BankAccount(CurrencyUnit.USD, QUERY_ACCOUNT_NAME);
	private BankAccount otherAccount = new BankAccount(CurrencyUnit.USD, OTHER_ACCOUNT_NAME);
	private Money money = Money.of(CurrencyUnit.USD, new BigDecimal(344.00));

	@Mock
	private TransactionRepository transactionRepo;
	
	@InjectMocks
	@Resource
	private AccountServiceImpl accountService;
	
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
	    
	}
	
	@Test
	public void testGetDebitTransactionsOf_all_transactions_are_debit() {
		CompletableFuture<List<Transaction>> transFuture = accountService.getDebitTransactionsOf(testAccountToQueryWith);
		
		transFuture.thenApply(transactions ->{
			for(Transaction t:transactions) {
				if(t.sourceAccount.getId() != testAccountToQueryWith.getId()) fail(
						"Transaction found where source (benefactor) account was not the account whose debit transactions were queried");
			}
			return null;
		});
	}
	
	@Test
	public void testGetDebitTransactionsOf_all_transactions_are_credit() {
		CompletableFuture<List<Transaction>> transFuture = accountService.getDebitTransactionsOf(testAccountToQueryWith);
		
		transFuture.thenApply(transactions ->{
			for(Transaction t:transactions) {
				if(t.beneficiaryAccount.getId() != testAccountToQueryWith.getId()) {
					System.out.println("AccountServiceTest | testGetDebitTransactionsOf_all_transactions_are_credit | credit trans found!");
					fail("Transaction found where beneficiary account was not the account whose credit transactions were queried");

				}
			}
			return null;
		});
	}

}
