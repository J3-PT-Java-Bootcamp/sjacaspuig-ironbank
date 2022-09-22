import { AccountStatus } from "../types/account-status";
import { AccountType } from "../types/account-type";
import { Money } from "./money";

export class Account {

    public iban?: string | undefined;
    public balance: Money;
    public primaryOwner: string;
    public secondaryOwner?: string | undefined;
    public secretKey: string;
    public creationDate?: Date | undefined;
    public status?: AccountStatus | undefined;
    public accountType?: AccountType | undefined;
    public lastMonthlyFeeDate?: Date | undefined;
    public creditLimit?: Money | undefined;
    public interestRate?: { value: string} | undefined;
    public interestRateDate?: Date | undefined;
    public minimumBalance?: Money | undefined;


    constructor(
        balance: Money,
        secretKey: string,
        primaryOwner: string,
        secondaryOwner?: string | undefined,
        status?: AccountStatus | undefined,
        iban?: string | undefined,
        creationDate?: Date | undefined,
        accountType?: AccountType | undefined,
        lastMonthlyFeeDate?: Date | undefined,
        creditLimit?: Money | undefined,
        interestRate?: { value: string} | undefined,
        interestRateDate?: Date | undefined,
        minimumBalance?: Money | undefined
    ) {
        this.iban = iban;
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.secretKey = secretKey;
        this.creationDate = creationDate;
        this.status = status;
        this.accountType = accountType;
        this.lastMonthlyFeeDate = lastMonthlyFeeDate;
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
        this.interestRateDate = interestRateDate;
        this.minimumBalance = minimumBalance;
    }
}