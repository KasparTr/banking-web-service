package com.danabijak.demo.banking.factories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.danabijak.demo.banking.GlobalMethodsForTesting;
import com.danabijak.demo.banking.core.ValidationReport;
import com.danabijak.demo.banking.domain.accounts.entity.BankAccount;
import com.danabijak.demo.banking.domain.users.entity.Role;
import com.danabijak.demo.banking.domain.users.entity.User;
import com.danabijak.demo.banking.domain.users.factories.UserFactoryImpl;
import com.danabijak.demo.banking.domain.users.services.UserServiceImpl;
import com.danabijak.demo.banking.domain.users.valueobjects.UserRequest;


public class UserFactoryImplTests {

	private static String VALID_PASSWORD_HASH = "öflkahdowidanlödawieuawioödnawdaölwdkl";
	
	
	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserFactoryImpl userFactory = new UserFactoryImpl();
	
	@org.junit.Before
	public void setUp() throws Exception {
		// Initialize mocks created above
	    MockitoAnnotations.initMocks(this);

	    when(passwordEncoder.encode(Mockito.any())).thenReturn(VALID_PASSWORD_HASH);
	}
	
	@Test
	public void testMakeDefaultBankingUser_user_is_active() {
		UserRequest userRequest = GlobalMethodsForTesting.getValidUserRequest();
		User testUser = userFactory.makeDefaultBankingUser(userRequest);

		assertTrue(testUser.isActive());
	}
	
	@Test
	public void testMakeDefaultBankingUser_user_balance_is_default() {
		UserRequest userRequest = GlobalMethodsForTesting.getValidUserRequest();
		User testUser = userFactory.makeDefaultBankingUser(userRequest);
		BigDecimal userBalance = testUser.getBankAccount().getBalance().getAmount();

		assertEquals(0, userBalance.compareTo(BankAccount.DEFAULT_LIMITS.BANKING_USER_START_BALANCE));
	}
	
	@Test
	public void testMakeAdminUser_user_role_includes_admin() {
		UserRequest userRequest = GlobalMethodsForTesting.getValidUserRequest();
		User testUser = userFactory.makeAdminUser(userRequest);
		boolean adminRoleFound = false;
		for(Role role : testUser.getRoles()) {
			if(role.getName() == Role.NAME.ADMIN) adminRoleFound = true;
		}
		assertTrue(adminRoleFound);
	}

}
