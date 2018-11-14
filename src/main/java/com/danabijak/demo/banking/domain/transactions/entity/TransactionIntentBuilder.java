package com.danabijak.demo.banking.domain.transactions.entity;


import org.joda.money.Money;

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
