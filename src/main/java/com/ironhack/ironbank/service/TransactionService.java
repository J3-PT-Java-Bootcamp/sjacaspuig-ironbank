package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.TransactionDTO;

import javax.validation.Valid;
import java.util.List;

public interface TransactionService {

    TransactionDTO create(@Valid TransactionDTO transactionDTO);
    TransactionDTO findById(@Valid Long id);
    List<TransactionDTO> findAll();
}
