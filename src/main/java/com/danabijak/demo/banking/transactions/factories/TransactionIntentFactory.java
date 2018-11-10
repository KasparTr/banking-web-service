package com.danabijak.demo.banking.transactions.factories;

import java.math.BigDecimal;
import java.util.Currency;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.entity.TransactionIntentStatus;
import com.danabijak.demo.banking.entity.TransactionIntentStatus.TRANSFER_STATUS;
import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.model.DepositClientRequest;
import com.danabijak.demo.banking.model.TransactionIntentBuilder;
import com.danabijak.demo.banking.model.TransactionIntentClientRequest;
import com.danabijak.demo.banking.services.UserService;

@Component
public class TransactionIntentFactory {
	

	public TransactionIntent createDepositRequest(User source, User beneficiary, DepositClientRequest request) {

		BigDecimal amountOfMoney = request.money.amount;
		Currency currency = request.money.currency;
		CurrencyUnit currencyUnit = CurrencyUnit.of(currency);
		Money money = Money.of(currencyUnit, amountOfMoney);

		
		return new TransactionIntentBuilder()
				.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Fresh from factory"))
				.beneficiary(beneficiary)
				.source(source)
				.amount(money)
				.build();
	}
	
	public TransactionIntent createFromClientRequest(User source, User beneficiary, TransactionIntentClientRequest request) {
		BigDecimal amountOfMoney = request.money.amount;
		Currency currency = request.money.currency;
		CurrencyUnit currencyUnit = CurrencyUnit.of(currency);
		Money money = Money.of(currencyUnit, amountOfMoney);

		
		return new TransactionIntentBuilder()
				.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Fresh from factory"))
				.beneficiary(beneficiary)
				.source(source)
				.amount(money)
				.build();
	}

}
