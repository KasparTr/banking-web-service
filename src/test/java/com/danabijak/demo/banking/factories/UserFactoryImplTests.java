package com.danabijak.demo.banking.factories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.danabijak.demo.banking.domain.accounts.entity.BankAccount;
import com.danabijak.demo.banking.domain.users.entity.Role;
import com.danabijak.demo.banking.domain.users.entity.User;
import com.danabijak.demo.banking.domain.users.factories.UserFactoryImpl;
import com.danabijak.demo.banking.domain.users.valueobjects.UserRequest;


public class UserFactoryImplTests {
	private static String FAULTY_USERNAME_EXAMPLE = "test";
	private static String VALID_USERNAME_EXAMPLE = "test@email.com";
	
	private static String FAULTY_PASSWORD_EXAMPLE = "1234";
	private static String VALID_PASSWORD_EXAMPLE = "pAS24@a3asd2KSH";
	
	private UserFactoryImpl userFactory = new UserFactoryImpl();
	
	@Test
	public void testMakeDefaultBankingUser_user_is_active() {
		UserRequest userRequest = new UserRequest(VALID_USERNAME_EXAMPLE, VALID_PASSWORD_EXAMPLE);
		User testUser = userFactory.makeDefaultBankingUser(userRequest);

		assertTrue(testUser.isActive());
	}
	
	@Test
	public void testMakeDefaultBankingUser_user_balance_is_default() {
		UserRequest userRequest = new UserRequest(VALID_USERNAME_EXAMPLE, VALID_PASSWORD_EXAMPLE);
		User testUser = userFactory.makeDefaultBankingUser(userRequest);
		BigDecimal userBalance = testUser.getBankAccount().getBalance().getAmount();

		assertEquals(0, userBalance.compareTo(BankAccount.DEFAULT_LIMITS.BANKING_USER_START_BALANCE));
	}
	
	@Test
	public void testMakeAdminUser_user_role_includes_admin() {
		UserRequest userRequest = new UserRequest(VALID_USERNAME_EXAMPLE, VALID_PASSWORD_EXAMPLE);
		User testUser = userFactory.makeAdminUser(userRequest);
		boolean adminRoleFound = false;
		for(Role role : testUser.getRoles()) {
			if(role.getName() == Role.NAME.ADMIN) adminRoleFound = true;
		}
		assertTrue(adminRoleFound);
	}

}
