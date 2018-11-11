package com.danabijak.demo.banking.validators;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.transactions.model.ValidationReport;

@Service
@Component
public interface UserValidatorService {
	
	public ValidationReport validateClientSentUser(User user);
	

}
