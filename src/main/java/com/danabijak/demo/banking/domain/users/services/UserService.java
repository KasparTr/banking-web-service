package com.danabijak.demo.banking.domain.users.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.danabijak.demo.banking.domain.users.entity.User;
import com.danabijak.demo.banking.domain.users.exceptions.UserNotFoundException;
import com.danabijak.demo.banking.domain.users.exceptions.UserObjectNotValidException;
import com.danabijak.demo.banking.domain.users.valueobjects.UserRequest;


@Service
public interface UserService {
    
	/**
	 * Create a new banking user who will be given a default Bank Account to operate with.
	 * @param user
	 * @return
	 * @throws UserObjectNotValidException
	 */
	@Async("asyncExecutor")
	public CompletableFuture<User> insertBanking(UserRequest user) throws UserObjectNotValidException;
	
	/**
	 * Create a new admin user.
	 * @param user
	 * @return
	 * @throws UserObjectNotValidException
	 */
	@Async("asyncExecutor")
	public CompletableFuture<User> insertAdmin(UserRequest user) throws UserObjectNotValidException;
	
	/**
	 * Create a new "Virtual Account" for the bank. This can be used to perform transaction operations on the behalf of the bank.
	 * @param user
	 * @return
	 */
	public User insertBankEntity(UserRequest user);
	
	/**
	 * Find user by id
	 * @param id
	 * @return
	 * @throws UserNotFoundException
	 */
	@Async("asyncExecutor")
	public CompletableFuture<User> find(long id) throws UserNotFoundException;
	
	
	/**
	 * Find user by username
	 * @param username
	 * @return
	 */
	@Async("asyncExecutor")
	public CompletableFuture<User> findByUsername(String username);
	
	/**
	 * Find all users in the system
	 * @return
	 */
	@Async("asyncExecutor")
	public CompletableFuture<List<User>> getAll();


}
