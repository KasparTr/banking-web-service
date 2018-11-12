package com.danabijak.demo.banking.accounts.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.danabijak.demo.banking.entity.BankAccount;
import com.danabijak.demo.banking.entity.User;

@Repository
public interface AccountRepository extends JpaRepository<BankAccount, Long>{
}

