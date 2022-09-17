package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.InterestRateResponse;
import com.ironhack.ironbank.dto.TransactionDTO;
import com.ironhack.ironbank.model.account.Account;

import javax.validation.Valid;
import java.util.List;

public interface TransactionService {

    TransactionDTO create(@Valid TransactionDTO transactionDTO);
    TransactionDTO findById(@Valid Long id);
    List<TransactionDTO> findAll();
    void createInterestTransaction(Account account, InterestRateResponse response);
    void createPenaltyMinBalanceTransaction(Account account);
}
