package com.ironhack.ironbank.repository;

import com.ironhack.ironbank.model.account.CurrentCheckingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentCheckingAccountRepository extends JpaRepository<CurrentCheckingAccount, String> {

}
