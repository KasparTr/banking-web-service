package com.danabijak.demo.banking.domain.transactions.valueobjects;

import java.math.BigDecimal;
import java.util.Currency;

public class MoneyRequest {
	public BigDecimal amount;
	public Currency currency;
	
	public MoneyRequest() {}

}
