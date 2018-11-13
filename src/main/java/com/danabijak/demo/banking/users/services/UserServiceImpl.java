package com.danabijak.demo.banking.users.services;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.infra.repositories.UserRepository;
import com.danabijak.demo.banking.transactions.model.ValidationReport;
import com.danabijak.demo.banking.users.exceptions.UserNotFoundException;
import com.danabijak.demo.banking.users.exceptions.UserObjectNotValidException;
import com.danabijak.demo.banking.users.factories.UserFactory;
import com.danabijak.demo.banking.validators.UserValidatorService;

@Service
public class UserServiceImpl implements UserService{
	
	
	private static final Logger log =  LoggerFactory.logger(UserService.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserFactory userFactory;
	
	
	@Autowired
	private UserValidatorService uvs;

	@Async("asyncExecutor")
	@Override
	public CompletableFuture<User> insertBanking(User user) throws UserObjectNotValidException{
		ValidationReport uvr = uvs.validateClientSentUser(user);
		if(uvr.valid) {
			user = userFactory.makeDefaultBankingUser(user);
			User savedUser = userRepository.save(user);
			log.info("New User was created: " + savedUser.getId());
			return CompletableFuture.completedFuture(user);
		}
		else
			throw new UserObjectNotValidException("User Object Not Valid. Errors: " + uvr.generateStringMessage());
    }
	
	@Async("asyncExecutor")
	@Override
	public CompletableFuture<User> insertAdmin(User user) throws UserObjectNotValidException {
		ValidationReport uvr = uvs.validateClientSentUser(user);
		if(uvr.valid) {
			user = userFactory.makeAdminUser(user);
			userRepository.save(user);
			log.info("New Admin was created: " + user.getId());
			return CompletableFuture.completedFuture(user);
		}
		else
			throw new UserObjectNotValidException("User Object Not Valid. Errors: " + uvr.generateStringMessage());
    }
	
	@Override
	public User insertBankEntity(User user) {
		ValidationReport uvr = uvs.validateClientSentUser(user);
		if(uvr.valid) {
			user = userFactory.makeBankEntity(user);
			userRepository.save(user);
			log.info("New Bank was created: " + user.getId());
			return user;
		}
		else
			throw new UserObjectNotValidException("User Object Not Valid. Errors: " + uvr.generateStringMessage());
    }
	
	@Async("asyncExecutor")
	@Override
	public CompletableFuture<User> find(long id) throws UserNotFoundException{
		Optional<User> user = userRepository.findById(id);
		
		if(user.isPresent()) {
			return CompletableFuture.completedFuture(user.get());
		}
		else {
			throw new UserNotFoundException(String.format("User with ID %s not found", id));
		}
    }
	
	@Async("asyncExecutor")
	@Override
	public CompletableFuture<User> findByUsername(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		
		if(user.isPresent())
			return CompletableFuture.completedFuture(user.get());
		else 
			throw new UserNotFoundException(String.format("User with username %s not found", username));
    }
	
	@Async("asyncExecutor")
	@Override
	public CompletableFuture<List<User>> getAll() {
		List<User> users = userRepository.findAll();

		if(users.isEmpty())
			throw new UserNotFoundException("No Users Found");
		else {
			return CompletableFuture.completedFuture(users);

		}
    }

}
