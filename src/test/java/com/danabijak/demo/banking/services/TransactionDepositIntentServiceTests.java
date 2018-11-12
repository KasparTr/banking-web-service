package com.danabijak.demo.banking.services;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
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
public class TransactionDepositIntentServiceTests {
	
	// Not using BeforeClass here because static methods don't work with @Autowired
	private static boolean setUpIsDone = false;
	
	private static String VALID_USERNAME_EXAMPLE = "test@email.com";
	private static String VALID_PASSWORD_EXAMPLE = "pAS24@a3asd2KSH";
	
	@Mock
	private TransactionIntentRepository transactionIntentRepo;

	@InjectMocks
	@Resource
	private DepositIntentService depositIntentService;

	
	@Test
	public void testAttemptPublish_valid_intent_is_saved_to_repo(){
		User beneficiary = getDummyUserWithBankAccountSetTo(3000);
		User source = getDummyUserWithBankAccountSetTo(3000);

		TransactionIntent validIntent = new TransactionIntentBuilder()
				.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Deposit"))
				.beneficiary(beneficiary)
				.source(source)
				.amount(Money.of(CurrencyUnit.USD, 123.45))
				.build();
		
		depositIntentService.attemptPublish(validIntent);
		
		verify(transactionIntentRepo).save(validIntent); 
		
	}
	
	@Test(expected = TransactionIntentPublishException.class)
	public void testAttemptPublish_invalid_intent_throws(){
		User beneficiary = getDummyUserWithBankAccountSetTo(30);
		User source = getDummyUserWithBankAccountSetTo(30);

		TransactionIntent invalidIntent = new TransactionIntentBuilder()
				.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Deposit"))
				.beneficiary(beneficiary)
				.source(source)
				.amount(Money.of(CurrencyUnit.USD, 12309133.45))
				.build();
		
		depositIntentService.attemptPublish(invalidIntent);		
	}

	private User getDummyUserWithBankAccountSetTo(int i) {
		User user = new User(VALID_USERNAME_EXAMPLE,VALID_PASSWORD_EXAMPLE);

		BankAccount ba = new BankAccount(BankAccount.DEFAULT_CURRENCY.USD, user.getUsername());
		ba.getBalance().setTotalAmount(new BigDecimal(i));
		user.attachBankAccount(ba);

		return user;
	}

}
