import { TransactionStatus } from "../types/transaction-status";
import { TransactionType } from "../types/transaction-type";
import { Money } from "./money";

export class Transaction {
    public id?: number | undefined;
    public sourceAccount?: string | undefined;
    public targetAccount?: string | undefined;
    public name: string;
    public amount: Money;
    public transactionDate?: Date | undefined;
    public status: TransactionStatus | undefined;
    public fee?: Money | undefined;
    public hashedKey?: string | undefined;
    public secretKey?: string | undefined;
    public concept?: string | undefined;
    public failureReason?: string | undefined;
    public type: TransactionType;
    public adminId: string;

    constructor(
        name: string,
        amount: Money,
        type: TransactionType,
        adminId: string,
        sourceAccount: string | undefined,
        targetAccount: string | undefined,
        fee: Money | undefined,
        secretKey: string | undefined,
        hashedKey: string | undefined,
        concept: string | undefined,
        id?: number | undefined,
        transactionDate?: Date | undefined,
        status?: TransactionStatus | undefined,
        failureReason?: string | undefined,

    ) {
        this.id = id;
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.name = name;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.status = status;
        this.fee = fee;
        this.hashedKey = hashedKey;
        this.secretKey = secretKey;
        this.concept = concept;
        this.failureReason = failureReason;
        this.type = type;
        this.adminId = adminId;
    }
    
    
}