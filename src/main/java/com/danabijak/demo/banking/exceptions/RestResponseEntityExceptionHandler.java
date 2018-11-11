package com.danabijak.demo.banking.exceptions;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.danabijak.demo.banking.users.exceptions.UserNotFoundException;
import com.danabijak.demo.banking.users.exceptions.UserSavingException;

@ControllerAdvice
@RestController
//TODO: Evaluate what goes to exception details for security purposes.
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) throws Exception {
		ExceptionResponse er = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity(er, HttpStatus.INTERNAL_SERVER_ERROR); 
		
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public final ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) throws Exception {
		ExceptionResponse er = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity(er, HttpStatus.NOT_FOUND); 
	}
	
	@ExceptionHandler(UserSavingException.class)
	public final ResponseEntity<Object> handleUserNotSavedException(UserSavingException ex, WebRequest request) throws Exception {
		ExceptionResponse er = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity(er, HttpStatus.INTERNAL_SERVER_ERROR); 
	}
	
}
