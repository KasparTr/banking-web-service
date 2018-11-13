package com.danabijak.demo.banking.services;

import javax.annotation.Resource;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.danabijak.demo.banking.infra.repositories.TransactionRepository;
import com.danabijak.demo.banking.transactions.services.TransactionServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTests {
	
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

}
