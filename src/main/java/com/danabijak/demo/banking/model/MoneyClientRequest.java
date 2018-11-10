package com.danabijak.demo.banking.model;

import java.math.BigDecimal;
import java.util.Currency;

import org.joda.money.CurrencyUnit;

public class MoneyClientRequest {
	public BigDecimal amount;
	public Currency currency;
	
	public MoneyClientRequest() {}

}
