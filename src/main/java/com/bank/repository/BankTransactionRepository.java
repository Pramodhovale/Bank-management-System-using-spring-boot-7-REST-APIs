package com.bank.repository;

import com.bank.entity.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankTransactionRepository extends JpaRepository<BankTransaction,Integer> {

    List<BankTransaction>
    findByAccountId_AccountIdOrderByTransactionDateDesc(Integer accountId);
}