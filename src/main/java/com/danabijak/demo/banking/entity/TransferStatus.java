package com.danabijak.demo.banking.entity;

public class TransferStatus {	
	
	public enum  TRANSFER_STATUS{
		OK(101), PENDING(201), FAIL(303), CANCEL(303);

	    private int numVal;

	    TRANSFER_STATUS(int numVal) {
	        this.numVal = numVal;
	    }

	    public int getNumVal() {
	        return numVal;
	    }
	}

	public TRANSFER_STATUS status;
	public String details;
	
	public TransferStatus(TRANSFER_STATUS status, String details) {
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
