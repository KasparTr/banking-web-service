package com.danabijak.demo.banking.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.joda.money.Money;

@Entity
public class Transaction {
	@Id
	@GeneratedValue
	public long id;
	public final Date created;
	
	public final Money amount;
	public final TransactionalEntity beneficiary;
	public final TransactionalEntity source;
	public final String details;
	
	public Transaction(Money amount, TransactionalEntity beneficiary, TransactionalEntity source, String details) {
		this.created = new Date();
		this.amount = amount;
		this.beneficiary = beneficiary;
		this.source = source;
		this.details = details;
		
    }
}
