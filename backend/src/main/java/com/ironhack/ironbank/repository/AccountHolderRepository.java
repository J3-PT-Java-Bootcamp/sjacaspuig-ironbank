package com.ironhack.ironbank.repository;

import com.ironhack.ironbank.model.user.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface AccountHolderRepository extends JpaRepository<AccountHolder, String> {

}
