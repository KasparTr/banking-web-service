package com.danabijak.demo.banking.entity;

import java.math.BigDecimal;
import java.util.Currency;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import com.danabijak.demo.banking.exceptions.BalanceOperationException;

@Entity
public class Balance {
	public static class DEFAULT_LIMITS{
		public static final BigDecimal DEFAULT_START_BALANCE = new BigDecimal(0.00);
		public static final BigDecimal BANKING_USER_START_BALANCE = new BigDecimal(100.00);
		public static final BigDecimal MAX_TOTAL_BALANCE = new BigDecimal(300000000.00);
	}
	
	@Id
	@GeneratedValue
	private long id;
	private Money total;

	
	public Balance(CurrencyUnit currency) {
		super();
		this.total = Money.of(currency, DEFAULT_LIMITS.DEFAULT_START_BALANCE);
	}

	public Money getTotal() {
		return total;
	}
	public void setTotalAmount(BigDecimal newTotalAmount) {
		CurrencyUnit cu = total.getCurrencyUnit();
		this.total = Money.of(cu, newTotalAmount);
	}
	
	public void increaseTotal(Money add) throws BalanceOperationException{
		Money tempTotal = total;
		this.total = total.plus(add);

		// If total is higher then the MAX LIMIT
		if(total.getAmount().compareTo(DEFAULT_LIMITS.MAX_TOTAL_BALANCE) == 1) {
			this.total = tempTotal;
			throw new BalanceOperationException(String.format("Balance cannot be over %s", DEFAULT_LIMITS.MAX_TOTAL_BALANCE));
		}
	}
	
	public void decreaseTotal(Money subtract) throws BalanceOperationException{
		if(subtract.isGreaterThan(total))
			throw new BalanceOperationException("Balance cannot be negative");
		
		this.total = total.minus(subtract);
	}



}
