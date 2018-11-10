package com.danabijak.demo.banking.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.joda.money.CurrencyUnit;
/*
 * BankAccount is the holder of the balance and can be attached to any transactional entity (e.g User, ATM, Application etc).
 * TODO: Currently there are no BankAccount related limits possible. Create them in future.
 */
@Entity
public class BankAccount {
	public static final CurrencyUnit DEFAULT_CURRENCY = CurrencyUnit.USD;

	
	public enum STATUS{
		ACTIVE, ARCHIVED, LOCKED, DELETED
	}
	
	@Id
	@GeneratedValue
	private long id;
	private String name;
	private Date created;
	
	@OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	private Balance balance;
	private STATUS status;
	
	public BankAccount(CurrencyUnit currency, String name) {
		super();
		this.name = name;
		this.created = new Date();
		this.balance = new Balance(currency);
		this.status = STATUS.ACTIVE;
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

	public Balance getBalance() {
		return balance;
	}

	public STATUS getStatus() {
		return status;
	}

	public void setStatus(STATUS status) {
		this.status = status;
	}
	
	
	
	
	
	
	

}
