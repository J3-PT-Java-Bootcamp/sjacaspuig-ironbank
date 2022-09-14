package com.ironhack.ironbank.constants;

import com.ironhack.ironbank.model.Money;

import java.math.BigDecimal;

public final class AccountConstants {

    private AccountConstants() {
    }

    public static final Money ACCOUNT_PENALTY_FEE = new Money(new BigDecimal("40"));
    public static final Money CREDIT_ACCOUNT_DEFAULT_CREDIT_LIMIT = new Money(new BigDecimal("100"));
    public static final Money CREDIT_ACCOUNT_MAX_CREDIT_LIMIT = new Money(new BigDecimal("100000"));
    public static final BigDecimal CREDIT_ACCOUNT_DEFAULT_INTEREST_RATE = new BigDecimal("0.2");
    public static final BigDecimal CREDIT_ACCOUNT_MIN_INTEREST_RATE = new BigDecimal("0.1");
    public static final Money CHECKING_ACCOUNT_MINIMUM_BALANCE = new Money(new BigDecimal("250"));
    public static final Money CHECKING_ACCOUNT_MONTHLY_MAINTENANCE_FEE = new Money(new BigDecimal("12"));
    public static final int CHECKING_ACCOUNT_MIN_AGE = 24;
    public static final BigDecimal SAVINGS_ACCOUNT_DEFAULT_INTEREST_RATE = new BigDecimal("0.0025");
    public static final BigDecimal SAVINGS_ACCOUNT_MAX_INTEREST_RATE = new BigDecimal("0.5");
    public static final Money SAVINGS_ACCOUNT_DEFAULT_MINIMUM_BALANCE = new Money(new BigDecimal("1000"));
    public static final Money SAVINGS_ACCOUNT_MIN_MINIMUM_BALANCE = new Money(new BigDecimal("100"));
    public static final Money GLOBAL_MINIMUM_BALANCE = new Money(new BigDecimal("0"));
}
