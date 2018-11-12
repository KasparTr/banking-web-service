package com.danabijak.demo.banking.users.contollers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties.Tomcat.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.infra.repositories.UserRepository;
import com.danabijak.demo.banking.transactions.http.AccountBalanceResponse;
import com.danabijak.demo.banking.transactions.http.BankAccountStatementClientResponse;
import com.danabijak.demo.banking.transactions.http.TransactionIntentClientResponse;
import com.danabijak.demo.banking.transactions.model.AccountTransactions;
import com.danabijak.demo.banking.transactions.services.TransactionService;
import com.danabijak.demo.banking.users.exceptions.UserNotFoundException;
import com.danabijak.demo.banking.users.exceptions.UserSavingException;
import com.danabijak.demo.banking.users.factories.BankAccountStatementFactory;
import com.danabijak.demo.banking.users.http.UserClientResponse;
import com.danabijak.demo.banking.users.services.UserService;

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
	
	
	@GetMapping("/services/users/{id}/account/{accountId}/balance")
	public CompletableFuture<ResponseEntity<AccountBalanceResponse>> getAccountBalance(@PathVariable long id, @PathVariable long accountId){
		// TODO: IMPLEMENT CHECK FOR TOKEN VS USER ID!!
		CompletableFuture<User> userFuture = userService.find(id);
		
		return userFuture.thenApply(user -> {
			// find account. Since currency user only has 1 account a search here is not required.
			BankAccount correctAccount = user.getBankAccount();

			return ResponseEntity.ok(new AccountBalanceResponse(correctAccount));
		});
		
	}
	
	@GetMapping("/services/users/{id}/account/{accountId}/statement")
	public CompletableFuture<ResponseEntity<BankAccountStatementClientResponse>> getAccountStatement(@PathVariable long id, @PathVariable long accountId){
		// IMPLEMENT CHECK FOR TOKEN VS USER ID!!
		CompletableFuture<User> userFuture = userService.find(id);
		
		return userFuture.thenApply(user -> {
			// find account. Since currency user only has 1 account a search here is not required.
			// this account would be used to search for transactions with the transactionService in the future
			BankAccount correctAccount = user.getBankAccount();
			
			BankAccountStatementClientResponse statement = baStatementFactory.generateStatement(
					user, 
					correctAccount, 
					transactionService.getTransactionsOf(correctAccount));

			return ResponseEntity.ok(statement);
		});
	}
	
	@PostMapping("/register")
	public ResponseEntity<UserClientResponse> createUser(@Valid @RequestBody User user) {
		User savedUser = userService.insertBanking(user);
		return ResponseEntity.ok(new UserClientResponse(savedUser.getId(), savedUser.getUsername(), "/oauth/token"));
		
	}

}
