package com.danabijak.demo.banking.users.validators;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.danabijak.demo.banking.transactions.model.ValidationReport;
import com.danabijak.demo.banking.users.entity.User;

@Service
@Component
public interface UserValidatorService {
	
	public ValidationReport validateClientSentUser(User user);
	

}
