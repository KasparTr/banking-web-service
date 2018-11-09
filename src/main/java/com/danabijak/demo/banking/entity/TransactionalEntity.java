package com.danabijak.demo.banking.entity;

import java.util.Currency;
import java.util.Locale;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import com.danabijak.demo.banking.exceptions.BankAccountException;

/*
 * TransactionalEntity is any actor in the system who is involved in transactions.
 * This can be for example a User, ATM, an Application or other.
 * Each TransactionalEntity will have entity based limits on transactions.
 * 
 */
@Entity
public abstract class TransactionalEntity extends BaseEntity {
	
	@OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	private EntityTransferLimits limits;
	
	@OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL, optional=true)
	private BankAccount bankAccount;
	private String name;
	
	public TransactionalEntity(String name) {
		super();
		this.name = name;
		this.limits = new EntityTransferLimits();
	}
	

	public BankAccount getBankAccount() throws BankAccountException{
		if(this.bankAccount == null)
				 throw new BankAccountException("No account attached to this entity");
		return this.bankAccount;
	}
	
	public void attachBankAccount(BankAccount bankAccount){
		this.bankAccount = bankAccount;
	}

}
