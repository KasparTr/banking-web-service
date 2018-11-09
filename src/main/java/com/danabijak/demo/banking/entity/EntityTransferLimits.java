package com.danabijak.demo.banking.entity;

import java.math.BigDecimal;
import java.util.Currency;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

/*
 * (Data Transfer Object)
 * EntityTransferLimits are entity (e.g User, ATM, App) specific limits that affect the transactions this entity is involved in.
 * NB! These limits will not overwrite any account or balance related limits.
 * Limits are set with default values.
 * Limits are currency based and currency shall be injected upon instantiation.
 * NOT TO BE MISTAKEN: With BankAccount limits that affect a specific Account, not an actor (user, atm, app, etc).!
 */
@Entity
public class EntityTransferLimits {
	public static class DEFAULT_LIMITS{
		public static final BigDecimal MAX_DAILY_WITHDRAWAL = new BigDecimal(3000.00);
		public static final BigDecimal MIN_WITHDRAWAL = new BigDecimal(10.00);
		
		public static final BigDecimal MAX_DAILY_DEPOSIT = new BigDecimal(3000.00); 
		public static final BigDecimal MIN_DEPOSIT = new BigDecimal(10.00);
	}
	
	@Id
	@GeneratedValue
	private long id;
	public final BigDecimal maxDailyWithdrawAmount;
	public final BigDecimal minWithdrawAmount;
	public final BigDecimal maxDailyDepositAmount;
	public final BigDecimal minDepositAmount;
	
	private BigDecimal availableForWithdrawalAmount;
	private BigDecimal availableToDepositAmount;
	
	/*
	 * Initiate with default values stated in class TransferLimits.DEFAULT_LIMITS
	 * Specify in which currency the limits are set.
	 */
	public EntityTransferLimits() {
		super();
		
		this.maxDailyWithdrawAmount = DEFAULT_LIMITS.MAX_DAILY_WITHDRAWAL;
		this.minWithdrawAmount = DEFAULT_LIMITS.MIN_WITHDRAWAL;
		this.maxDailyDepositAmount = DEFAULT_LIMITS.MAX_DAILY_DEPOSIT;
		this.minDepositAmount = DEFAULT_LIMITS.MIN_DEPOSIT;
		
		this.availableForWithdrawalAmount = this.maxDailyWithdrawAmount;
		this.availableToDepositAmount = this.maxDailyDepositAmount;
	}
	
	public void resetMoneyAvailable() {
		this.availableForWithdrawalAmount = this.maxDailyWithdrawAmount;
		this.availableToDepositAmount = this.maxDailyDepositAmount;
	}
	
	public void decreaseAvailableForWithdrawal(BigDecimal amount) {
		this.availableForWithdrawalAmount = availableForWithdrawalAmount.add(amount);
	}
	
	public void increaseAvailableForWithdrawal(BigDecimal amount) {
		this.availableForWithdrawalAmount = availableForWithdrawalAmount.add(amount);
	}
	
	public void decreaseAvailableToDeposit(BigDecimal amount) {
		this.availableToDepositAmount = availableToDepositAmount.add(amount);
	}
	
	public void increaseAvailableToDeposit(BigDecimal amount) {
		this.availableToDepositAmount = availableToDepositAmount.add(amount);
	}
	
	public BigDecimal getAvailableForWithdrawal() {
		return this.availableForWithdrawalAmount;
	}
	
	public BigDecimal getAvailableToDeposit() {
		return this.availableToDepositAmount;
	}
	
}
