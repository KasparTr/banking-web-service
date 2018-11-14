package com.danabijak.demo.banking.domain.transactions.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.joda.money.Money;

import com.danabijak.demo.banking.domain.accounts.entity.BankAccount;

@Entity
public class Transaction {
	@Id
	@GeneratedValue
	public long id;
	public final Date created;
	
	public final Money amount;
	
	@OneToOne(fetch = FetchType.EAGER)
	public final BankAccount beneficiaryAccount;
	
	@OneToOne(fetch = FetchType.EAGER)
	public final BankAccount sourceAccount;
	
	public final String details;
	
	Transaction(){
		this.created = null;
		this.amount = null;
		this.beneficiaryAccount = null;
		this.sourceAccount = null;
		this.details = null;
	}
	
	public Transaction(Money amount, BankAccount beneficiary, BankAccount source, String details) {
		this.created = new Date();
		this.amount = amount;
		this.beneficiaryAccount = beneficiary;
		this.sourceAccount = source;
		this.details = details;
    }
	
	public String toString() {
		return "Transaction{" + "amount=" + amount.toString()+ ", beneficiary=" + beneficiaryAccount.getName() + 
				", source=" + sourceAccount.getName() + 
				", details=" + details + '}';

	}
}
