package com.danabijak.demo.banking.model;

import com.danabijak.demo.banking.users.http.TransactionalEntityClientR;

public class DepositClientRequest {
	public TransactionalEntityClientR depositor;
	public MoneyClientRequest money;

}
