package com.danabijak.demo.banking.services;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.factories.UserFactory;


/**
 * This will create an initial bank entity which will be used as the source for deposits.
 * @author kaspar
 *
 */
@Component
public class BankServiceCommandlineRunner implements CommandLineRunner{
	
	private static final Logger log =  LoggerFactory.logger(UserServiceCommandlineRunner.class);
	
	@Autowired
	private UserService userService;


	
	@Override
	public void run(String... args) throws Exception {
		createBankEntity();
	}
	
	private void createBankEntity() {
		try {
			userService.insertBankEntity(new User(
		              "bankItself@bank.com",
		              "bankItSElf123@sad"));
		}catch(Exception e) {
			log.error("Bank User not created: " + e.getMessage());
		}
		
	}
}