package com.ironhack.ironbank.model.account;

import com.ironhack.ironbank.model.Money;
import com.ironhack.ironbank.model.user.AccountHolder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "accounts")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Account {

    @Embedded
    protected static final Money PENALTY_FEE = new Money(new BigDecimal("40"));

    @Id
    private String iban;

    @NotNull
    @Embedded
    private Money balance;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "primary_owner_id")
    protected AccountHolder primaryOwner;

    @ManyToOne
    @JoinColumn(name = "secondary_owner_id")
    protected AccountHolder secondaryOwner;

}
