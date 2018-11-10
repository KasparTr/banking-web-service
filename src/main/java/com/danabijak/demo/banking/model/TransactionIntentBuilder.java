package com.danabijak.demo.banking.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import org.joda.money.Money;

import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.entity.TransactionalEntity;
import com.danabijak.demo.banking.entity.TransactionIntentStatus;

public class TransactionIntentBuilder {
	
	public Money amount;
	public TransactionIntentStatus status;
	public TransactionalEntity beneficiary;	//to
	public TransactionalEntity source;			//from

    //builder methods for setting property
    public TransactionIntentBuilder amount(Money amount){this.amount = amount; return this; }
    public TransactionIntentBuilder status(TransactionIntentStatus status){this.status = status; return this; }
    public TransactionIntentBuilder beneficiary(TransactionalEntity beneficiary){this.beneficiary = beneficiary; return this; }
    public TransactionIntentBuilder source(TransactionalEntity source){this.source= source; return this; }
 
    //return fully build object
    public TransactionIntent build() {
        return new TransactionIntent(this);
    }

}
