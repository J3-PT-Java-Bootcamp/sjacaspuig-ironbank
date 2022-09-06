package com.ironhack.ironbank.repository;

import com.ironhack.ironbank.model.account.CurrentSavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentSavingsAccountRepository extends JpaRepository<CurrentSavingsAccount, String> {

}
