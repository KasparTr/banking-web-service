package com.danabijak.demo.banking.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties.Tomcat.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import org.springframework.hateoas.*;

import com.danabijak.demo.banking.entity.BankAccount;
import com.danabijak.demo.banking.entity.Role;
import com.danabijak.demo.banking.entity.Transaction;
import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.exceptions.UserNotFoundException;
import com.danabijak.demo.banking.exceptions.UserSavingException;
import com.danabijak.demo.banking.repositories.UserRepository;
import com.danabijak.demo.banking.services.UserService;
import com.danabijak.demo.banking.transactions.http.AccountBalanceResponse;
import com.danabijak.demo.banking.transactions.http.BankAccountStatementClientResponse;
import com.danabijak.demo.banking.transactions.services.TransactionService;
import com.danabijak.demo.banking.users.factories.BankAccountStatementFactory;

@RestController
public class UserController {
	
//	@Autowired
//	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private BankAccountStatementFactory baStatementFactory;
	
	
	@GetMapping("/services/users")
	public ResponseEntity<List<User> > getAllUsers(){
		return ResponseEntity.ok(userService.getAll());
	}
	
	@GetMapping("/services/users/{id}")
	public ResponseEntity<User> findById(@PathVariable long id){
		return ResponseEntity.ok(userService.find(id));
	}
	
	@GetMapping("/services/users/{id}/account/balance")
	public ResponseEntity<AccountBalanceResponse> getAccountBalance(@PathVariable long id){
		// IMPLEMENT CHECK FOR TOKEN VS USER ID!!
		User user = userService.find(id);
		
		// find account. Since currency user only has 1 account a search here is not required.
		BankAccount correctAccount = user.getBankAccount();

		return ResponseEntity.ok(new AccountBalanceResponse(correctAccount));
	}
	
	@GetMapping("/services/users/{id}/account/{accountId}/statement")
	public ResponseEntity<BankAccountStatementClientResponse> getAccountStatement(@PathVariable long id, @PathVariable long accountId){
		// IMPLEMENT CHECK FOR TOKEN VS USER ID!!
		User user = userService.find(id);		
		// find account. Since currency user only has 1 account a search here is not required.
		// this account would be used to search for transactions with the transactionService in the future
		BankAccount correctAccount = user.getBankAccount();
		
		List<Transaction> dTransactions = transactionService.getDebitTransactionsOf(correctAccount);
		List<Transaction> cTransactions = transactionService.getCreditTransactionsOf(correctAccount);
		
		BankAccountStatementClientResponse statement = baStatementFactory.generateStatement(user, correctAccount.getId(), cTransactions, dTransactions);

		return ResponseEntity.ok(statement);
	}
	
	@PostMapping("/register")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		User savedUser = userService.insertBanking(user);
		
		return ResponseEntity.ok(savedUser);
		
		
//		URI location = ServletUriComponentsBuilder
//			.fromCurrentRequest()
//			.path("/{id}")
//			.buildAndExpand(savedUser.getId()).toUri();
//		
//		return ResponseEntity.created(location).build();
		
	}
	
	
//	@PostMapping(value="/register")
//	public ResponseEntity<User> createNewUser(@RequestBody User newUser) {
//		System.out.println("C_USER | createNewUser()");
//		
//		User user = attemptNewUserSave(newUser);
//		//if (user == null)
//			//return ResponseEntity.noContent().build();
//
//		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
//				"/{id}").buildAndExpand(user.getId()).toUri();
//
//		return ResponseEntity.created(location).build();
//		
//	}
//	
//	private User attemptNewUserSave(User newUser) {
//		try {
//			UserService uService = new UserService();
//
//			return uService.save(new User(
//					newUser.getUsername(),
//					newUser.getPassword(),
//	                Arrays.asList(new Role("USER"), new Role("ACTUATOR")), 
//	                true
//	        ));
//		}catch(Exception use) {
//			System.out.println(use.getMessage());
//			throw new UserSavingException(use.getMessage());
//		}
//	}
}
