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

@Component
@Service
public interface UserService {
    
	@Async("asyncExecutor")
	public CompletableFuture<User> insertBanking(User user) throws UserObjectNotValidException;
	
	@Async("asyncExecutor")
	public CompletableFuture<User> insertAdmin(User user) throws UserObjectNotValidException;
	
	public User insertBankEntity(User user);
	
	@Async("asyncExecutor")
	public CompletableFuture<User> find(long id) throws UserNotFoundException;
	
	@Async("asyncExecutor")
	public CompletableFuture<User> findByUsername(String username);
	
	@Async("asyncExecutor")
	public CompletableFuture<List<User>> getAll();


}
