package com.danabijak.demo.banking.services;

import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.model.UserValidationReport;

public interface UserValidatorService {
	
	public UserValidationReport validateClientSentUser(User user);
	

}
