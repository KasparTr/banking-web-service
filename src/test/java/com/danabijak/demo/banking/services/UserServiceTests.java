package com.danabijak.demo.banking.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.annotation.Resource;

import org.aspectj.lang.annotation.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.danabijak.demo.banking.infra.repositories.UserRepository;
import com.danabijak.demo.banking.transactions.entity.TransactionIntentStatus;
import com.danabijak.demo.banking.users.entity.Role;
import com.danabijak.demo.banking.users.entity.User;
import com.danabijak.demo.banking.users.exceptions.UserNotFoundException;
import com.danabijak.demo.banking.users.exceptions.UserObjectNotValidException;
import com.danabijak.demo.banking.users.services.UserService;
import com.danabijak.demo.banking.users.services.UserServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTests {
	
	// Not using BeforeClass here because static methods don't work with @Autowired
	private static boolean setUpIsDone = false;
	
	private static long EXISTING_USER_ID = 2000;
	private static long NON_EXISTING_USER_ID = 4040;
	
	private static String FAULTY_USERNAME_EXAMPLE = "test";
	private static String VALID_USERNAME_EXAMPLE = "test@email.com";
	
	private static String FAULTY_PASSWORD_EXAMPLE = "1234";
	private static String VALID_PASSWORD_EXAMPLE = "pAS24@a3asd2KSH";
	
	
	@Mock
	private UserRepository userRepository;

	@InjectMocks
	@Resource
	private UserServiceImpl userService;
	
	@org.junit.Before
	public void setUp() throws Exception {
		// Initialize mocks created above
	    MockitoAnnotations.initMocks(this);
	    
	    
	    // Change Mocks behavior for user queries
	    User nUser = new User(VALID_USERNAME_EXAMPLE, VALID_PASSWORD_EXAMPLE);
	    when(userRepository.findById(EXISTING_USER_ID)).thenReturn(Optional.of(nUser));
	    
	    Optional<User> emptyUser = Optional.empty();
	    when(userRepository.findById(NON_EXISTING_USER_ID)).thenReturn(emptyUser);	    
    
	}
	
	@Test(expected = UserObjectNotValidException.class)
	public void testInsertBanking_throw_user_object_faulty() {
		User testUser = new User(FAULTY_USERNAME_EXAMPLE, FAULTY_PASSWORD_EXAMPLE);
		userService.insertBanking(testUser);
	}
	
	@Test
	public void testInsertBanking_repo_is_invoked() {
		User testUser = new User(VALID_USERNAME_EXAMPLE, VALID_PASSWORD_EXAMPLE);
		userService.insertBanking(testUser);
		verify(userRepository).save(testUser); 
	}
	
	
	@Test
	public void testInsertAdmin_repo_is_invoked() {
		User testUser = new User(VALID_USERNAME_EXAMPLE, VALID_PASSWORD_EXAMPLE);
		userService.insertAdmin(testUser);
		verify(userRepository).save(testUser); 
	}

	
	@Test(expected = UserNotFoundException.class)
	public void testFind_throw_UserNotFoundException_if_no_user() throws Throwable {
		
		CompletableFuture<User> future = userService.find(NON_EXISTING_USER_ID);
		// The CompletableFuture wraps the real exception cause with ExecutionException. 
		// We must catch that and see what caused that.
		try {
			future.get();
		} catch (InterruptedException | ExecutionException e) {
			throw e.getCause();
		}
	}
	
	@Test(expected = UserNotFoundException.class)
	public void testGetAll_throw_UserNotFoundException_if_empty() throws Throwable{
		List<User> returnableUsers = new ArrayList<>();

		when(userRepository.findAll()).thenReturn(returnableUsers);

		CompletableFuture<List<User>> future = userService.getAll();
		// The CompletableFuture wraps the real exception cause with ExecutionException. 
		// We must catch that and see what caused that.
		try {
			future.get();
		} catch (InterruptedException | ExecutionException e) {
			throw e.getCause();
		}
	}
	
	@Test
	public void testGetAll_return_users_list() {
		List<User> returnableUsers = new ArrayList<>();
		returnableUsers.add(new User(VALID_USERNAME_EXAMPLE, VALID_PASSWORD_EXAMPLE));
		
	    when(userRepository.findAll()).thenReturn(returnableUsers);

	    CompletableFuture<List<User>> usersFuture = userService.getAll();
	    usersFuture.thenAccept(users -> {
	    	assertTrue(users.size() > 0);
		});
		
	}
}
