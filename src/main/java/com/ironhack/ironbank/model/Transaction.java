package com.ironhack.ironbank.model;

import com.ironhack.ironbank.dto.TransactionDTO;
import com.ironhack.ironbank.enums.TransactionStatus;
import com.ironhack.ironbank.enums.TransactionType;
import com.ironhack.ironbank.model.account.Account;
import com.ironhack.ironbank.model.user.Admin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    @CreatedBy
    private Admin admin;

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

    @NotNull
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TransactionType type;

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

    private String concept;
    private String failureReason;

    public Transaction() {
        setType(TransactionType.TRANSFER);
        setStatus(TransactionStatus.PENDING);
    }

    public static Transaction fromDTO(TransactionDTO transactionDTO, Account sourceAccount, Account targetAccount) {
        var transaction = new Transaction();
        transaction.setId(transactionDTO.getId());
        transaction.setName(transactionDTO.getName());
        var amount = Money.fromDTO(transactionDTO.getAmount());
        transaction.setAmount(amount);
        if (transactionDTO.getStatus() != null) {
            transaction.setStatus(transactionDTO.getStatus());
        }

        // check if fee is null
        if (transactionDTO.getFee() != null && transactionDTO.getFee().getAmount() != null) {
            var fee = Money.fromDTO(transactionDTO.getFee());
            transaction.setFee(fee);
        } else {
            var zeroFee = new Money(new BigDecimal(0));
            transaction.setFee(zeroFee);
        }


        if (transactionDTO.getType() != null) {
            transaction.setType(transactionDTO.getType());
        }

        transaction.setHashedKey(transactionDTO.getHashedKey());
        transaction.setSecretKey(transactionDTO.getSecretKey());
        transaction.setSourceAccount(sourceAccount);
        transaction.setTargetAccount(targetAccount);
        transaction.setConcept(transactionDTO.getConcept());
        transaction.setFailureReason(transactionDTO.getFailureReason());
        return transaction;
    }
}
