package com.danabijak.demo.banking.services;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.model.UserValidationReport;

@Service
@Component
public interface ValidatorService {
	
	public UserValidationReport validateClientSentUser(User user);
	

}
