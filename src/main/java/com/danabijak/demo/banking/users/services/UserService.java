package com.danabijak.demo.banking.users.services;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.infra.repositories.UserRepository;
import com.danabijak.demo.banking.transactions.model.ValidationReport;
import com.danabijak.demo.banking.users.exceptions.UserNotFoundException;
import com.danabijak.demo.banking.users.exceptions.UserObjectNotValidException;
import com.danabijak.demo.banking.users.factories.UserFactory;
import com.danabijak.demo.banking.validators.UserValidatorService;


@Service
public interface UserService {
    
	/**
	 * Create a new banking user who will be given a default Bank Account to operate with.
	 * @param user
	 * @return
	 * @throws UserObjectNotValidException
	 */
	@Async("asyncExecutor")
	public CompletableFuture<User> insertBanking(User user) throws UserObjectNotValidException;
	
	/**
	 * Create a new admin user.
	 * @param user
	 * @return
	 * @throws UserObjectNotValidException
	 */
	@Async("asyncExecutor")
	public CompletableFuture<User> insertAdmin(User user) throws UserObjectNotValidException;
	
	/**
	 * Create a new "Virtual Account" for the bank. This can be used to perform transaction operations on the behalf of the bank.
	 * @param user
	 * @return
	 */
	public User insertBankEntity(User user);
	
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
