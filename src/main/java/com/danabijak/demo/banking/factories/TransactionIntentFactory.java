package com.danabijak.demo.banking.factories;

import java.math.BigDecimal;
import java.util.Currency;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.entity.TransferStatus;
import com.danabijak.demo.banking.entity.TransferStatus.TRANSFER_STATUS;
import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.model.DepositClientRequest;
import com.danabijak.demo.banking.model.TransactionIntentBuilder;
import com.danabijak.demo.banking.model.TransactionIntentClientRequest;
import com.danabijak.demo.banking.services.UserService;

@Component
public class TransactionIntentFactory {
	
	@Autowired
	private UserService userService;
	
	public TransactionIntent createDepositRequest(DepositClientRequest request) {
		
		User bank = userService.findByUsername("bankItself@bank.com");
		User beneficiary = userService.find(request.depositor.id);
		BigDecimal amountOfMoney = request.money.amount;
		Currency currency = request.money.currency;
		CurrencyUnit currencyUnit = CurrencyUnit.of(currency);
		Money money = Money.of(currencyUnit, amountOfMoney);

		
		return new TransactionIntentBuilder()
				.status(new TransferStatus(TRANSFER_STATUS.CREATED, "Fresh from factory"))
				.beneficiary(beneficiary)
				.source(bank)
				.amount(money)
				.build();
	}
	
	public TransactionIntent createFromClientRequest(TransactionIntentClientRequest request) {
		
		User source = userService.find(request.source.id);
		User beneficiary = userService.find(request.beneficiary.id);
		BigDecimal amountOfMoney = request.money.amount;
		Currency currency = request.money.currency;
		CurrencyUnit currencyUnit = CurrencyUnit.of(currency);
		Money money = Money.of(currencyUnit, amountOfMoney);

		
		return new TransactionIntentBuilder()
				.status(new TransferStatus(TRANSFER_STATUS.CREATED, "Fresh from factory"))
				.beneficiary(beneficiary)
				.source(source)
				.amount(money)
				.build();
	}

}
