package com.ironhack.ironbank.model.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.ironbank.dto.AccountDTO;
import com.ironhack.ironbank.enums.AccountType;
import com.ironhack.ironbank.interfaces.PenaltyFee;
import com.ironhack.ironbank.model.Money;
import com.ironhack.ironbank.model.Transaction;
import com.ironhack.ironbank.model.user.AccountHolder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.Set;

import static com.ironhack.ironbank.constants.AccountConstants.ACCOUNT_PENALTY_FEE;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "accounts")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Account implements PenaltyFee {

    private static final Money PENALTY_FEE = ACCOUNT_PENALTY_FEE;

    @Id
    protected String iban;

    @NotNull
    @Embedded
    protected Money balance;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "primary_owner_id")
    protected AccountHolder primaryOwner;

    @ManyToOne
    @JoinColumn(name = "secondary_owner_id")
    protected AccountHolder secondaryOwner;

    @NotNull
    @Enumerated(EnumType.STRING)
    protected AccountType accountType;

    @Column(name = "secret_key")
    private String secretKey;

    @OneToMany(
            mappedBy = "sourceAccount",
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private Set<Transaction> sourceTransactions;

    @OneToMany(
            mappedBy = "targetAccount",
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private Set<Transaction> targetTransactions;


    public static Account fromDTO(AccountDTO accountDTO, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        var account = new Account();
        account.setIban(accountDTO.getIban());

        var money = Money.fromDTO(accountDTO.getBalance());
        account.setBalance(money);

        account.setPrimaryOwner(primaryOwner);

        if (accountDTO.getSecondaryOwner() != null) {
            account.setSecondaryOwner(secondaryOwner);
        }

        account.setAccountType(accountDTO.getAccountType());

        if (accountDTO.getSecretKey() != null) {
            account.setSecretKey(accountDTO.getSecretKey());
        }

        return account;
    }

    public void setBalance(Money balance, Money minimumBalance) {
        // First check if minimum balance is not null
        if (minimumBalance != null) {
            // If balance is less than minimum balance, apply penalty fee
            if (balance.getAmount().compareTo(minimumBalance.getAmount()) < 0) {
                this.balance = chargePenaltyFee(balance, PENALTY_FEE);
            } else { // If balance is greater than minimum balance, set balance
                this.balance = balance;
            }
        }
    }
}
