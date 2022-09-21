package com.ironhack.ironbank.repository;

import com.ironhack.ironbank.model.account.CurrentSavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrentSavingsAccountRepository extends JpaRepository<CurrentSavingsAccount, String> {

    List<CurrentSavingsAccount> findAllByPrimaryOwnerIdOrSecondaryOwnerId(String primaryOwnerId, String secondaryOwnerId);
}
