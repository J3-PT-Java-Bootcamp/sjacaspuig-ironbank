package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.enums.TransactionStatus;
import com.ironhack.ironbank.model.Money;
import com.ironhack.ironbank.model.Transaction;
import com.ironhack.ironbank.model.account.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionDTO {

    private Long id;
    private String sourceAccount;
    private String targetAccount;
    private String name;
    private MoneyDTO amount;
    private Instant transactionDate;
    private TransactionStatus status;
    private MoneyDTO fee;
    private String hashedKey;
    private String secretKey;
    private String concept;

    public static TransactionDTO fromEntity(Transaction transaction) {
        var transactionDTO = new TransactionDTO();
        transactionDTO.setId(transaction.getId());
        transactionDTO.setSourceAccount(transaction.getSourceAccount().getIban());
        transactionDTO.setTargetAccount(transaction.getTargetAccount().getIban());
        transactionDTO.setName(transaction.getName());
        var amountDTO = MoneyDTO.fromEntity(transaction.getAmount());
        transactionDTO.setAmount(amountDTO);
        transactionDTO.setTransactionDate(transaction.getTransactionDate());
        transactionDTO.setStatus(transaction.getStatus());
        if (transaction.getFee() != null) {
            var feeDTO = MoneyDTO.fromEntity(transaction.getFee());
            transactionDTO.setFee(feeDTO);
        }
        transactionDTO.setHashedKey(transaction.getHashedKey());
        transactionDTO.setSecretKey(transaction.getSecretKey());
        transactionDTO.setConcept(transaction.getConcept());
        return transactionDTO;
    }

    public static List<TransactionDTO> fromEntities(List<Transaction> transactions) {
        return transactions.stream().map(TransactionDTO::fromEntity).toList();
    }
}
