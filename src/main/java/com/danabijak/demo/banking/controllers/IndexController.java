package com.danabijak.demo.banking.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

	
	@GetMapping(value="/")
	public String index() {
		return "Hello";
	}
	
	@GetMapping(value="/services")
	public String services() {
		return "This is Protected Services Area";
	}
	
}
