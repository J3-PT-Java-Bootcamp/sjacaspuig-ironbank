package com.ironhack.ironbank.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.ironbank.model.Address;
import com.ironhack.ironbank.model.account.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "account_holders")
public class AccountHolder extends User {

    @NotNull
    @Column(name = "date_of_birth")
    private Instant birthDate;

    @NotNull
    @Embedded
    private Address primaryAddress;

    @Embedded
    @AttributeOverrides(
            {
                    @AttributeOverride(name = "street", column = @Column(name = "secondary_street")),
                    @AttributeOverride(name = "number", column = @Column(name = "secondary_number")),
                    @AttributeOverride(name = "extraInformation", column = @Column(name = "secondary_extra_information")),
                    @AttributeOverride(name = "postalCode", column = @Column(name = "secondary_postal_code")),
                    @AttributeOverride(name = "country", column = @Column(name = "secondary_country")),
                    @AttributeOverride(name = "city", column = @Column(name = "secondary_city")),
                    @AttributeOverride(name = "province", column = @Column(name = "secondary_province"))
            }
    )
    private Address secondaryAddress;


    @OneToMany(
            mappedBy = "primaryOwner",
            cascade = CascadeType.ALL,
            orphanRemoval= true
    )
    @JsonIgnore
    private Set<Account> primaryAccounts;

    @OneToMany(
            mappedBy = "secondaryOwner",
            cascade = CascadeType.ALL,
            orphanRemoval= true
    )
    @JsonIgnore
    private Set<Account> secondaryAccounts;
}