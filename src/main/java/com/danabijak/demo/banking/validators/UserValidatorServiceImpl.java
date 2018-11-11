package com.danabijak.demo.banking.validators;

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
import com.danabijak.demo.banking.infra.repositories.UserRepository;
import com.danabijak.demo.banking.transactions.model.ValidationReport;

@Component
public class UserValidatorServiceImpl implements UserValidatorService{
	
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public ValidationReport validateClientSentUser(User user) {
		ValidationReport report;
		boolean isUserValid = true;
		List<String> faults = new ArrayList<>();
		
		if(isUserTaken(user)){
			isUserValid = false;
			faults.add("Username taken");
		}
		
		
		List<String> passwordFaults = getPasswordFaults(user.getPassword());
		if(passwordFaults != null){
			isUserValid = false;
			faults.addAll(passwordFaults);
		}
		
				
		if(!isUsernameValid(user.getUsername())){
        	isUserValid = false;
        	faults.add("Username must be a valid email");
        }
	 
		 
		if(!isUserValid) {
			Optional<List<String>> faultyParts = Optional.of(faults);
			report = new ValidationReport(false, faultyParts);
		}else 
			report = new ValidationReport(true);
		
		return report;
	}
	
	public boolean isUsernameValid(String username) {
		Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(username);
        return mat.matches();
	}
	
	public List<String> getPasswordFaults(String password) {
		PasswordValidator validator = new PasswordValidator(Arrays.asList(
				new LengthRule(8,20),
				new UppercaseCharacterRule(1),
				new LowercaseCharacterRule(1),
				new DigitCharacterRule(1),
				new SpecialCharacterRule(1)
			));
		RuleResult result = validator.validate(new PasswordData(password));
		
		if(result.isValid())
			return null;
		else
			return validator.getMessages(result);
	}
	
	public boolean isUserTaken(User user) {
		Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
		if(existingUser.isPresent()) return true;
		
		return false;
		
	}

	

}
