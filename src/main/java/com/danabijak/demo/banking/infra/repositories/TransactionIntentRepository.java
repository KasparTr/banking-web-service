package com.danabijak.demo.banking.infra.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.danabijak.demo.banking.transactions.entity.TransactionIntent;
import com.danabijak.demo.banking.users.entity.User;

@Repository
public interface TransactionIntentRepository extends JpaRepository<TransactionIntent, Long>{
}
