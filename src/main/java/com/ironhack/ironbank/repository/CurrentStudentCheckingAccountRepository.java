package com.ironhack.ironbank.repository;

import com.ironhack.ironbank.model.account.CurrentStudentCheckingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrentStudentCheckingAccountRepository extends JpaRepository<CurrentStudentCheckingAccount, String> {

    List<CurrentStudentCheckingAccount> findAllByPrimaryOwnerIdOrSecondaryOwnerId(String primaryOwnerId, String secondaryOwnerId);
}
