package com.ironhack.ironbank.repository;

import com.ironhack.ironbank.model.account.CreditAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditAccountRepository extends JpaRepository<CreditAccount, String> {

}
