package com.danabijak.demo.banking.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.aspectj.lang.annotation.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.exceptions.UserNotFoundException;
import com.danabijak.demo.banking.repositories.UserRepository;
import com.danabijak.demo.banking.services.UserService;
import com.danabijak.demo.banking.entity.Role;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTests {
	
	// Not using BeforeClass here because static methods don't work with @Autowired
	private static boolean setUpIsDone = false;
	
	private static long EXISTING_USER_ID = 2000;
	private static long NON_EXISTING_USER_ID = 4040;
	
	@Mock
	private UserRepository userRepository;

	@InjectMocks
	@Resource
	private UserService userService;
	
	@org.junit.Before
	public void setUp() throws Exception {
	    // Initialize mocks created above
	    MockitoAnnotations.initMocks(this);
	    setUpIsDone = true;
	    
	    // Change Mocks behavior for user queries
	    User nUser = new User("existingUserName", "existingUserPsw");
	    when(userRepository.findById(EXISTING_USER_ID)).thenReturn(Optional.of(nUser));
	    
	    Optional<User> emptyUser = Optional.empty();
	    when(userRepository.findById(NON_EXISTING_USER_ID)).thenReturn(emptyUser);
	}
	
	
	@Test
	public void testInsertActive_repo_is_invoked() {
		User testUser = new User("testName", "testPassword");
		userService.insertActive(testUser);
		verify(userRepository).save(testUser); 
	}
	
	@Test
	public void testInsertActive_user_is_active() {
		User testUser = new User("testName", "testPassword");
		userService.insertActive(testUser);
		assertTrue(testUser.isActive());
	}
	
	@Test
	public void testInsertAdmin_repo_is_invoked() {
		User testUser = new User("testAdminName", "testAdminPassword");
		userService.insertAdmin(testUser);
		verify(userRepository).save(testUser); 
	}
	
	@Test
	public void testInsertAdmin_user_is_admin() {
		User testUser = new User("testName", "testPassword");
		User aUser = userService.insertAdmin(testUser);
		boolean adminRoleFound = false;
		for(Role role : aUser.getRoles()) {
			System.out.println(role.getName().toString());
			if(role.getName() == Role.NAME.ADMIN) adminRoleFound = true;
		}
		assertTrue(adminRoleFound);
	}
	
	@Test
	public void testFind_correct_user_is_found() {
		User foundUser = userService.find(EXISTING_USER_ID);
		assertEquals(foundUser.getUsername(), "existingUserName");
	}
	
	@Test(expected = UserNotFoundException.class)
	public void testFind_throw_UserNotFoundException_if_no_user() {
		userService.find(NON_EXISTING_USER_ID);
	}
	
	@Test(expected = UserNotFoundException.class)
	public void testGetAll_throw_UserNotFoundException_if_empty() {
	    when(userRepository.findAll()).thenReturn(new ArrayList<User>());

		userService.getAll();
	}
	
	@Test
	public void testGetAll_return_users_list() {
		List<User> returnableUsers = new ArrayList<>();
		returnableUsers.add(new User("nonom", "nonm"));
		
	    when(userRepository.findAll()).thenReturn(returnableUsers);

		List<User> users = userService.getAll();
		assertTrue(users.size() > 0);
	}
	

}
