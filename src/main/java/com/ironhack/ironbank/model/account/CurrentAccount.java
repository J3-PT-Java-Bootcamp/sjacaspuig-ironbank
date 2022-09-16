package com.ironhack.ironbank.model.account;

import com.ironhack.ironbank.enums.AccountStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
public abstract class CurrentAccount extends Account {

    @CreationTimestamp
    @Column(name = "creation_date", updatable = false)
    private Instant creationDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    public CurrentAccount() {}
}
