package com.danabijak.demo.banking.infra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.danabijak.demo.banking.transactions.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{
}