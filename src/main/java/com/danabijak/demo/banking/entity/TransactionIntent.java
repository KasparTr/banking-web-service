package com.danabijak.demo.banking.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import org.joda.money.Money;

import com.danabijak.demo.banking.model.TransactionIntentBuilder;

/**
 * TransactionIntent is an intent for a future Transaction.
 * TransactionIntent is a prepared transaction statement that is processed in future, but not yet finalized or paid.
 * TransactionIntent is used to realize a Transaction.
 */
@Entity
public class TransactionIntent {
	// Required upon creation
	@Id
	@GeneratedValue
	private long id;
	
	private final Money amount;
	private final Date createdAt;
	
	@OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	private final TransferStatus status;
	
	@OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	private final TransactionalEntity beneficiary;	//to
	
	@OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	private final TransactionalEntity source;			//from
	
	// Can be set later
	private boolean sent;
	private boolean paid;
	private Date sentAt;
	private Date processedAt;
	private Money fee;
	private boolean isValid;
	private String details;
	private Date transferable;

	
	public TransactionIntent(TransactionIntentBuilder builder) {
        this.amount = builder.amount;
        this.createdAt = new Date();
        this.status = builder.status;
        this.beneficiary = builder.beneficiary;
        this.source = builder.source;     
    }
	
	public void setSentTo(boolean sent) {
		this.sent = sent;
	}
	
	public void setPaidTo(boolean paid) {
		this.paid = paid;
	}
	
	public void setSentAt(Date sentAt) {
		this.sentAt = sentAt;
	}
	
	public void setPaidAt(Date processedAt) {
		this.processedAt = processedAt;
	}
	
	public void setFee(Money fee) {
		this.fee = fee;
	}
	
	public void setIsValidTransfer(boolean isValid) {
		this.isValid = isValid;
	}
	
	/**
	 * If transfer scheduling is added, use this to set the preferred transfer date.
	 * @param date
	 */
	public void setTransferable(Date transferable) {
		this.transferable = transferable;
	}


}
