package com.danabijak.demo.banking.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import org.joda.money.Money;

import com.danabijak.demo.banking.transactions.model.TransactionIntentBuilder;

/**
 * TransactionIntent is an intent for a future Transaction.
 * TransactionIntent is a prepared transaction statement that is processed in future, but not yet finalized or paid.
 * TransactionIntent is used to realize a Transaction.
 * If TransactionIntent is set to false via the setValidToFalse() method, it cannot be changed back to true and a new intent must be created.
 */
@Entity
public class TransactionIntent {
	// Required upon creation
	@Id
	@GeneratedValue
	public long id;
	
	public final Money amount;
	public final Date createdAt;
	
	@OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	public final TransactionIntentStatus status;
	
	@OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	public final TransactionalEntity beneficiary;	//to
	
	@OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	public final TransactionalEntity source;			//from
	
	// Can be set later
	private boolean sent;
	private boolean paid;
	private Date publishedAt;
	private Date processedAt;
	private Money fee;
	private boolean isValid;
	private boolean allowToSetValid;
	private String details;
	private Date transferable;

	TransactionIntent(){
		this.amount= null;
		this.createdAt = null;
		this.status = null;
		this.beneficiary = null;
		this.source = null;
	}
	
	public TransactionIntent(TransactionIntentBuilder builder) {
        this.amount = builder.amount;
        this.createdAt = new Date();
        this.status = builder.status;
        this.beneficiary = builder.beneficiary;
        this.source = builder.source;
        this.isValid = false;			// this needs to be set true by the TransactionIntentValidator only
        this.allowToSetValid = true;
    }
	
	public boolean isValid() {
		return this.isValid;
	}
	
	public void setSentTo(boolean sent) {
		this.sent = sent;
	}
	
	public void setPaidTo(boolean paid) {
		this.paid = paid;
	}
	
	public void setPublishedAt(Date sentAt) {
		this.publishedAt = sentAt;
	}
	
	public void setPaidAt(Date processedAt) {
		this.processedAt = processedAt;
	}
	
	public void setFee(Money fee) {
		this.fee = fee;
	}
	

	public void setIntentAsValid() {
		if(this.allowToSetValid) this.isValid = true;
	}
	
	/**
	 * Attention! If invoked, a TransactionIntent cannot be set back to valid, and a new intent must be created.
	 */
	public void setIntentAsNotValid() {
		this.isValid = false;
		this.allowToSetValid= false;
	}
	
	/**
	 * If transfer scheduling is added, use this to set the preferred transfer date.
	 * @param date
	 */
	public void setTransferable(Date transferable) {
		this.transferable = transferable;
	}
	
	public String toString() {
		return "source: " + this.source.getName() + ", " + "beneficiary: " + this.beneficiary.getName() + ", " + "isValid: " + this.isValid;
	}

	public TransactionalEntity getBeneficiary() {
		return beneficiary;
	}

	public TransactionalEntity getSource() {
		return source;
	}
	
	
	

}
