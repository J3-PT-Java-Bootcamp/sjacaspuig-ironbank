package com.ironhack.ironbank.repository;

import com.ironhack.ironbank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySourceAccountIbanOrTargetAccountIban(String sourceAccountIban, String targetAccountIban);
    List<Transaction> findBySourceAccountPrimaryOwnerIdOrSourceAccountSecondaryOwnerIdOrTargetAccountPrimaryOwnerIdOrTargetAccountSecondaryOwnerId(String firstId, String secondId, String thirdId, String fourthId);
}
