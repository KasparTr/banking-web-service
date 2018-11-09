package com.danabijak.demo.banking.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.passay.DigitCharacterRule;
import org.passay.LengthRule;
import org.passay.LowercaseCharacterRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.SpecialCharacterRule;
import org.passay.UppercaseCharacterRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.model.UserValidationReport;
import com.danabijak.demo.banking.repositories.UserRepository;

@Component
public class UserValidatorServiceImpl implements UserValidatorService{
	
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserValidationReport validateClientSentUser(User user) {
		
		// TODO: Break this method down
		
		UserValidationReport report;
		boolean isUserValid = true;
		List<String> faults = new ArrayList<>();
		
		// check if User exists
		Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
		if(existingUser.isPresent()) {
			isUserValid = false;
			faults.add("Username exists");
		}
		
		// check for password
		PasswordValidator validator = new PasswordValidator(Arrays.asList(
				new LengthRule(8,20),
				new UppercaseCharacterRule(1),
				new LowercaseCharacterRule(1),
				new DigitCharacterRule(1),
				new SpecialCharacterRule(1)
			));
		RuleResult result = validator.validate(new PasswordData(user.getPassword()));
		if(!result.isValid()){
			isUserValid = false;
			faults.addAll(validator.getMessages(result));
		}
		
		// check for username aka email
		Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(user.getUsername());

        if(!mat.matches()){
        	isUserValid = false;
        	faults.add("username must be a valid email");
        }
		
		if(!isUserValid) {
			Optional<List<String>> faultyParts = Optional.of(faults);
			report = new UserValidationReport(false, faultyParts);
		}else {
			report = new UserValidationReport(true);

		}
		return report;
	}

	

}
