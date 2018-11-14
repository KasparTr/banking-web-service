package com.danabijak.demo.banking.domain.transactions.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntent;

@Repository
public interface TransactionIntentRepository extends JpaRepository<TransactionIntent, Long>{
}
