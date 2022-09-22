package com.ironhack.ironbank.repository;

import com.ironhack.ironbank.model.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

}
