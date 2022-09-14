package com.ironhack.ironbank.model;

import com.ironhack.ironbank.dto.TransactionDTO;
import com.ironhack.ironbank.enums.TransactionStatus;
import com.ironhack.ironbank.model.account.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "source_account_id")
    private Account sourceAccount;

    @ManyToOne()
    @JoinColumn(name = "target_account_id")
    private Account targetAccount;

    @NotNull
    private String name;

    @NotNull
    private Money amount;

    @CreationTimestamp
    @Column(name = "transaction_date")
    private Instant transactionDate;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @AttributeOverrides(
            {
                    @AttributeOverride(name = "currency", column = @Column(name = "fee_currency")),
                    @AttributeOverride(name = "amount", column = @Column(name = "fee_amount")),
            }
    )
    private Money fee;

    @Column(name = "hashed_key")
    private String hashedKey;

    @Column(name = "secret_key")
    private String secretKey;

    public static Transaction fromDTO(TransactionDTO transactionDTO, Account sourceAccount, Account targetAccount) {
        var transaction = new Transaction();
        transaction.setId(transactionDTO.getId());
        transaction.setName(transactionDTO.getName());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setStatus(transactionDTO.getStatus());
        transaction.setFee(transactionDTO.getFee());
        transaction.setHashedKey(transactionDTO.getHashedKey());
        transaction.setSecretKey(transactionDTO.getSecretKey());
        transaction.setSourceAccount(sourceAccount);
        transaction.setTargetAccount(targetAccount);
        return transaction;
    }
}
