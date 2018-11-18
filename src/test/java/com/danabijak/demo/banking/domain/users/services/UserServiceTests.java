package com.danabijak.demo.banking.domain.users.services;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.danabijak.demo.banking.GlobalMethodsForTesting;
import com.danabijak.demo.banking.core.ValidationReport;
import com.danabijak.demo.banking.domain.users.entity.User;
import com.danabijak.demo.banking.domain.users.exceptions.UserNotFoundException;
import com.danabijak.demo.banking.domain.users.exceptions.UserObjectNotValidException;
import com.danabijak.demo.banking.domain.users.factories.UserFactory;
import com.danabijak.demo.banking.domain.users.repositories.UserRepository;
import com.danabijak.demo.banking.domain.users.services.UserServiceImpl;
import com.danabijak.demo.banking.domain.users.validators.UserValidatorService;
import com.danabijak.demo.banking.domain.users.valueobjects.UserRequest;

public class UserServiceTests {
	
	
	private static long EXISTING_USER_ID = 2000;
	private static long NON_EXISTING_USER_ID = 4040;
	
	private static String FAULTY_USERNAME_EXAMPLE = "test";
	private static String VALID_USERNAME_EXAMPLE = "test@email.com";
	
	private static String FAULTY_PASSWORD_EXAMPLE = "1234";
	private static String VALID_PASSWORD_EXAMPLE = "pAS24@a3asd2KSH";
	
	private final UserRequest validUserRequest = new UserRequest(VALID_USERNAME_EXAMPLE, VALID_PASSWORD_EXAMPLE);
	private final UserRequest faultyUserRequest = new UserRequest(FAULTY_USERNAME_EXAMPLE, FAULTY_PASSWORD_EXAMPLE);

	@Mock
	private UserRepository userRepository;
	
	@Mock
	private UserValidatorService uvs;
	
	@Mock
	private UserFactory userFactory;

	@InjectMocks
	private UserServiceImpl userService = new UserServiceImpl();
	
	@org.junit.Before
	public void setUp() throws Exception {
		// Initialize mocks created above
	    MockitoAnnotations.initMocks(this);
	    
	    // Change Mocks behavior for user queries
	    User nUser = new User(VALID_USERNAME_EXAMPLE, VALID_PASSWORD_EXAMPLE);
	    when(userRepository.findById(EXISTING_USER_ID)).thenReturn(Optional.of(nUser));
	    
	    Optional<User> emptyUser = Optional.empty();
	    when(userRepository.findById(NON_EXISTING_USER_ID)).thenReturn(emptyUser);	   
	    
	    ValidationReport validReport = new ValidationReport(true);
	    when(uvs.validateClientSentUser(validUserRequest)).thenReturn(validReport);
	    
	    List<String> faults = new ArrayList<>();
	    faults.add("Test Fault");
	    Optional<List<String>> faultDescriptions = Optional.of(faults);
	    ValidationReport faultyReport = new ValidationReport(false, faultDescriptions);
	    when(uvs.validateClientSentUser(faultyUserRequest)).thenReturn(faultyReport);
	    
	    User user = GlobalMethodsForTesting.getDummyDefaultUser();
	    when(userFactory.makeAdminUser(validUserRequest)).thenReturn(user);
	    when(userFactory.makeBankEntity(validUserRequest)).thenReturn(user);
	    when(userFactory.makeDefaultBankingUser(validUserRequest)).thenReturn(user);

    
	}
	
	@Test(expected = UserObjectNotValidException.class)
	public void testInsertBanking_fauly_user_request_throws() {
		userService.insertBanking(faultyUserRequest);
	}
	
	@Test
	public void testInsertBanking_repo_is_invoked() {
		CompletableFuture<User> userFuture = userService.insertBanking(validUserRequest);
		try {
			User user = userFuture.get();
			verify(userRepository).save(user); 
		} catch (Exception e) {
			fail();
		}
	}
	
	
	@Test
	public void testInsertAdmin_repo_is_invoked() {
		CompletableFuture<User> userFuture = userService.insertAdmin(validUserRequest);
		try {
			User user = userFuture.get();
			verify(userRepository).save(user); 
		} catch (Exception e) {
			fail();
		}
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
		try {
			assertTrue(usersFuture.get().size() > 0);
		} catch (Exception e) {
			fail();
		}
		
	}
}
