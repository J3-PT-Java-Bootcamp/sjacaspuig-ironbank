package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.enums.TransactionStatus;
import com.ironhack.ironbank.enums.TransactionType;
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
    private String failureReason;
    private TransactionType type;
    private String adminId;

    public TransactionDTO() {
        setType(TransactionType.TRANSFER);
    }

    public static TransactionDTO fromEntity(Transaction transaction) {
        var transactionDTO = new TransactionDTO();
        transactionDTO.setId(transaction.getId());

        // Check if sourceAccount is null
        if (transaction.getSourceAccount() != null) {
            transactionDTO.setSourceAccount(transaction.getSourceAccount().getIban());
        }

        // Check if targetAccount is null
        if (transaction.getTargetAccount() != null) {
            transactionDTO.setTargetAccount(transaction.getTargetAccount().getIban());
        }

        if (transaction.getType() != null) {
            transactionDTO.setType(transaction.getType());
        }
        transactionDTO.setName(transaction.getName());
        var amountDTO = MoneyDTO.fromEntity(transaction.getAmount());
        transactionDTO.setAmount(amountDTO);
        transactionDTO.setTransactionDate(transaction.getTransactionDate());
        transactionDTO.setStatus(transaction.getStatus());
        if (transaction.getFee() != null && transaction.getFee().getAmount() != null) {
            var feeDTO = MoneyDTO.fromEntity(transaction.getFee());
            transactionDTO.setFee(feeDTO);
        }
        transactionDTO.setHashedKey(transaction.getHashedKey());
        transactionDTO.setSecretKey(transaction.getSecretKey());
        transactionDTO.setConcept(transaction.getConcept());
        transactionDTO.setFailureReason(transaction.getFailureReason());
        if(transaction.getAdmin() != null) {
            transactionDTO.setAdminId(transaction.getAdmin().getId());
        }

        return transactionDTO;
    }

    public static List<TransactionDTO> fromEntities(List<Transaction> transactions) {
        return transactions.stream().map(TransactionDTO::fromEntity).toList();
    }
}
