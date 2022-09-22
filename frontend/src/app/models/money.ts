export class Money {
    public amount: string;
    public currency?: string | undefined;

    constructor(amount: string, currency?: string) {
        this.amount = amount;
        this.currency = currency;
    }

    
}