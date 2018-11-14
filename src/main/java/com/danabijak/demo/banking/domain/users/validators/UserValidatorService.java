package com.danabijak.demo.banking.domain.users.validators;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.danabijak.demo.banking.core.ValidationReport;
import com.danabijak.demo.banking.domain.users.valueobjects.UserRequest;

@Service
@Component
public interface UserValidatorService {
	
	public ValidationReport validateClientSentUser(UserRequest user);
	

}
