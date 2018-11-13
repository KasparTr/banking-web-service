package com.danabijak.demo.banking.transactions.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class TransactionIntentStatus {	
	
	public enum  TRANSFER_STATUS{
		CREATED(010), OK(101), PENDING(201), FAIL(303), CANCEL(303);

	    private int numVal;

	    TRANSFER_STATUS(int numVal) {
	        this.numVal = numVal;
	    }

	    public int getNumVal() {
	        return numVal;
	    }
	}
	@Id
	@GeneratedValue
	public long id;

	public TRANSFER_STATUS status;
	public String details;
	
	TransactionIntentStatus(){}
	
	public TransactionIntentStatus(TRANSFER_STATUS status, String details) {
		this.status = status;
		this.details = details;
	}
	
	public void setStatus(TRANSFER_STATUS status) {
		this.status= status;
	}
	
	public void setDetails(String details) {
		this.details = details;
	}
}
