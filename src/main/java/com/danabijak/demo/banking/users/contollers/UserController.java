package com.danabijak.demo.banking.users.contollers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.concurrent.CompletableFuture;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.danabijak.demo.banking.accounts.entity.BankAccount;
import com.danabijak.demo.banking.accounts.http.AccountBalanceResponse;
import com.danabijak.demo.banking.users.entity.User;
import com.danabijak.demo.banking.users.http.UserClientResponse;
import com.danabijak.demo.banking.users.services.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	
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
	
	@PostMapping("/register")
	public CompletableFuture<ResponseEntity<UserClientResponse>> createUser(@Valid @RequestBody User user) {
		CompletableFuture<User> userFuture = userService.insertBanking(user);
		return userFuture.thenApply(savedUser -> {
			return ResponseEntity.ok(new UserClientResponse(
					savedUser.getId(), 
					savedUser.getUsername(), 
					"/oauth/token",			//TODO: Replace hardcoded link to login with dynamic variable
					savedUser.getBankAccount().getId()));

		});
		
	}

}
