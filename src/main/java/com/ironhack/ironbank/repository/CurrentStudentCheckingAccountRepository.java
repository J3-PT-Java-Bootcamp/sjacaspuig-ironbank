package com.ironhack.ironbank.repository;

import com.ironhack.ironbank.model.account.CurrentStudentCheckingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentStudentCheckingAccountRepository extends JpaRepository<CurrentStudentCheckingAccount, String> {

}
