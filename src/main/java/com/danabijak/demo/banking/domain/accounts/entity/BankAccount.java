package com.danabijak.demo.banking.domain.accounts.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import com.danabijak.demo.banking.domain.transactions.exceptions.BalanceOperationException;
/*
 * BankAccount is the holder of the balance and can be attached to any transactional entity (e.g User, ATM, Application etc).
 * TODO: Currently there are no BankAccount related limits possible. Create them in future.
 */
@Entity
public class BankAccount {
	
	public static class DEFAULT_LIMITS{
		public static final BigDecimal DEFAULT_START_BALANCE = new BigDecimal(0.00);
		public static final BigDecimal BANKING_USER_START_BALANCE = new BigDecimal(100.00);
		public static final BigDecimal MAX_TOTAL_BALANCE = new BigDecimal(300000000.00);
	}
	public static final CurrencyUnit DEFAULT_CURRENCY = CurrencyUnit.USD;

	
	public enum STATUS{
		ACTIVE, ARCHIVED, LOCKED, DELETED
	}
	
	@Id
	@GeneratedValue
	private long id;
	private String name;
	private Date created;

	private STATUS status;
	private Money balance;
	
	BankAccount(){}
	
	public BankAccount(CurrencyUnit currency, String name) {
		super();
		this.name = name;
		this.created = new Date();
		this.balance = Money.of(currency, DEFAULT_LIMITS.DEFAULT_START_BALANCE);
		this.status = STATUS.ACTIVE;
	}
	
	public Money getBalance() {
		return this.balance;
	}
	
	public void setBalance(BigDecimal newTotalAmount) {
		CurrencyUnit cu = balance.getCurrencyUnit();
		this.balance = Money.of(cu, newTotalAmount);
	}
	
	public void increaseBalance(Money add) throws BalanceOperationException{
		Money tempBalance = balance;
		this.balance = balance.plus(add);

		// If total is higher then the MAX LIMIT
		if(balance.getAmount().compareTo(DEFAULT_LIMITS.MAX_TOTAL_BALANCE) == 1) {
			this.balance = balance;
			throw new BalanceOperationException(String.format("Balance cannot be over %s", DEFAULT_LIMITS.MAX_TOTAL_BALANCE));
		}
	}
	
	public void decreaseBalance(Money subtract) throws BalanceOperationException{
		if(subtract.isGreaterThan(balance))
			throw new BalanceOperationException("Balance cannot be negative");
		
		this.balance = balance.minus(subtract);
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreated() {
		return created;
	}

	public STATUS getStatus() {
		return status;
	}

	public void setStatus(STATUS status) {
		this.status = status;
	}
	
	
	
	
	
	
	

}
