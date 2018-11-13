package com.danabijak.demo.banking.transactions.entity;

import java.math.BigDecimal;
import java.util.Currency;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

/**
 * EntityTransferLimits are entity (e.g User, ATM, App) specific limits that affect the transactions this entity is involved in.
 * NB! These limits will not overwrite any account or balance related limits.
 * Limits are set with default values.
 * Limits are not currency based and so they will append to any currency the BankAccount my be subjected to.
 * NOT TO BE MISTAKEN: With BankAccount limits that affect a specific Account, not an entity (user, atm, app, etc).!
 * Props:
 * 	allowedWithdrawalAmount - this is the max amount that an entity could potentially withdraw at any point in time (other limits will apply)
 * 	allowedDepositAmount - -||- deposit
 * 
 * NB! These limits do not depend on the Balance of a bank account or other factors that may affect the overall transaction limitations.
 * (Not all entities have a bank account but all entities will have some limits to their operations)
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
	
	private BigDecimal allowedWithdrawalAmount;
	private BigDecimal allowedDepositAmount;
	
	/*
	 * Initiate with default values stated in class TransferLimits.DEFAULT_LIMITS
	 * Specify in which currency the limits are set.
	 * TODO: Set up constraints on the allowed limits (cannot be negative)
	 */
	public EntityTransferLimits() {
		super();
		
		this.maxDailyWithdrawAmount = DEFAULT_LIMITS.MAX_DAILY_WITHDRAWAL;
		this.minWithdrawAmount = DEFAULT_LIMITS.MIN_WITHDRAWAL;
		this.maxDailyDepositAmount = DEFAULT_LIMITS.MAX_DAILY_DEPOSIT;
		this.minDepositAmount = DEFAULT_LIMITS.MIN_DEPOSIT;
		
		this.allowedWithdrawalAmount = this.maxDailyWithdrawAmount;
		this.allowedDepositAmount = this.maxDailyDepositAmount;
	}
	
	public void resetAllowedLimits() {
		this.allowedWithdrawalAmount = this.maxDailyWithdrawAmount;
		this.allowedDepositAmount = this.maxDailyDepositAmount;
	}
	
	public void decreaseAllowedWithdrawal(BigDecimal amount) {
		this.allowedWithdrawalAmount = allowedWithdrawalAmount.subtract(amount);
	}
	
	public void increaseAllowedWithdrawal(BigDecimal amount) {
		this.allowedWithdrawalAmount = allowedWithdrawalAmount.add(amount);
	}
	
	public void decreaseAllowedDeposit(BigDecimal amount) {
		this.allowedDepositAmount = allowedDepositAmount.subtract(amount);
	}
	
	public void increaseAllowedDeposit(BigDecimal amount) {
		this.allowedDepositAmount = allowedDepositAmount.add(amount);
	}
	
	public BigDecimal getAllowedWithdrawal() {
		return this.allowedWithdrawalAmount;
	}
	
	public BigDecimal getAllowedDeposit() {
		return this.allowedDepositAmount;
	}
	
}
