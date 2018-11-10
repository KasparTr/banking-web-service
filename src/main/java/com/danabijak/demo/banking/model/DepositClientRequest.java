package com.danabijak.demo.banking.model;

import com.danabijak.demo.banking.users.http.responses.TransactionalEntityClientR;

public class DepositClientRequest {
	public TransactionalEntityClientR depositor;
	public MoneyClientRequest money;

}
